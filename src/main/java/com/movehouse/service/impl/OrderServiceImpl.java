package com.movehouse.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movehouse.common.PageParam;
import com.movehouse.common.PageResult;
import com.movehouse.entity.*;
import com.movehouse.enums.UserTypeEnum;
import com.movehouse.mapper.OrderMapper;
import com.movehouse.service.ComplaintService;
import com.movehouse.service.DriverService;
import com.movehouse.service.OrderService;
import com.movehouse.service.UserService;
import com.movehouse.util.DbUtil;
import com.movehouse.util.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static com.movehouse.common.MoveHouseException.exception;
import static com.movehouse.enums.UserTypeEnum.DRIVER;
import static com.movehouse.enums.UserTypeEnum.USER;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Resource
    private DriverService driverService;
    @Resource
    private UserService userService;
    @Resource
    private ComplaintService complaintService;

    @Transactional
    @Override
    public void payment(Publish publish) {
        User user = userService.getById(publish.getUserId());
        Driver driver = driverService.getById(publish.getDriverId());

        //校验用户余额
        if (user.getBalance().compareTo(publish.getValuation()) < 0) {
            throw exception(500, "用户余额不足，请先前往个人中心充值!");
        }
        //暂扣用户余额（平台托管）
        user.setBalance(user.getBalance().subtract(publish.getValuation()));

        Order order = new Order();
        String orderNo = RandomUtil.randomNumbers(12);

        // 核心修复：把所有搬家详细信息拷贝到订单表中
        order.setOrderNo(orderNo)
                .setUserId(user.getId())
                .setUsername(user.getUsername())
                .setDriverId(driver.getId())
                .setDriverName(driver.getName())
                .setPrice(publish.getValuation())
                .setAddress(publish.getAddress())
                .setOriginAddress(publish.getOriginAddress())
                .setDestinationAddress(publish.getDestinationAddress())
                .setCargoDescription(publish.getGoodsDesc())
                .setDistance(publish.getDistance())
                .setStatus(0) // 状态置为0，代表等待司机前往
                .setCreateTime(new Date());

        save(order);
        userService.updateById(user);
    }

    @Override
    public PageResult<Order> pageOrder(PageParam param) {
        UserTypeEnum userType = UserHolder.getUserType();
        Long id = UserHolder.getId();
        Page<Order> page = lambdaQuery()
                //用户只能看到自己的订单
                .eq(userType.equals(USER), Order::getUserId, id)
                //司机也只能看到自己的订单
                .eq(userType.equals(DRIVER), Order::getDriverId, id)
                .orderByDesc(Order::getCreateTime)
                .page(param.page());

        page.getRecords().forEach(e -> {
            boolean exists = complaintService.lambdaQuery()
                    .eq(Complaint::getOrderId, e.getId())
                    .exists();
            e.setIsComplaint(exists);
        });
        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    @Transactional
    @Override
    public void changeStatus(Long id, Integer status) {
        Order order = DbUtil.checkNotFound(id, this);
        if (status == 0) {
            // 司机已到达
            order.setArriveTime(new Date()).setStatus(1);
        } else {
            // 核心修复：订单彻底完成, 将订单的钱“累加”转入司机账户，防止覆盖
            Driver driver = driverService.getById(order.getDriverId());
            BigDecimal currentIncome = driver.getIncome() == null ? BigDecimal.ZERO : driver.getIncome();
            driver.setIncome(currentIncome.add(order.getPrice()));
            driverService.updateById(driver);

            order.setFinishTime(new Date()).setStatus(2);
        }
        updateById(order);
    }
}