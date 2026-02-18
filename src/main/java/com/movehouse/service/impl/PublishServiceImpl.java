package com.movehouse.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movehouse.common.LoginUser;
import com.movehouse.common.PageParam;
import com.movehouse.common.PageResult;
import com.movehouse.controller.vo.PublishValuationVo;
import com.movehouse.entity.Publish;
import com.movehouse.enums.UserTypeEnum;
import com.movehouse.mapper.PublishMapper;
import com.movehouse.service.OrderService;
import com.movehouse.service.PublishService;
import com.movehouse.util.DbUtil;
import com.movehouse.util.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static com.movehouse.enums.UserTypeEnum.DRIVER;
import static com.movehouse.enums.UserTypeEnum.USER;


@Service
public class PublishServiceImpl extends ServiceImpl<PublishMapper, Publish> implements PublishService {
    @Resource
    private OrderService orderService;

    @Override
    public void create(Publish publish) {
        LoginUser loginUser = UserHolder.get();
        publish.setUserId(loginUser.getId())
                .setUsername(loginUser.getUsername())
                .setCreateTime(new Date());
        save(publish);
    }

    @Override
    public PageResult<Publish> pagePublish(PageParam param) {
        LoginUser loginUser = UserHolder.get();
        String keyword = param.getKeyword();

        // 采用分步构建 Wrapper 的方式
        LambdaQueryWrapper<Publish> wrapper = new LambdaQueryWrapper<>();

        // 1. 关键字搜索逻辑 (标题或地址)
        if (StrUtil.isNotBlank(keyword)) {
            // 这里为了保证 SQL 语句生成正确的括号 (title LIKE ? OR address LIKE ?)，使用一次简短的 lambda
            wrapper.and(w -> w.like(Publish::getTitle, keyword).or().like(Publish::getAddress, keyword));
        }

        // 2. 根据角色身份分配查询条件
        if (loginUser == null) {
            // 【访客逻辑】：未登录的情况下，只能看到大厅里待接单的信息
            wrapper.eq(Publish::getStatus, 0);
        } else {
            // 【已登录逻辑】
            UserTypeEnum userType = UserHolder.getUserType();
            // 在这里获取 currentUserId，它只被赋值一次，满足 Java "实际上的最终变量" 要求，彻底解决报错
            Long currentUserId = UserHolder.getId();

            if (USER.equals(userType)) {
                // 普通用户：只能看到自己发布的单子
                wrapper.eq(Publish::getUserId, currentUserId);

            } else if (DRIVER.equals(userType)) {
                // 司机：能看到待接单的信息，或者自己已经出价抢单的信息
                wrapper.and(w -> w.eq(Publish::getStatus, 0).or().eq(Publish::getDriverId, currentUserId));
            }
        }

        // 3. 按发布时间倒序排列
        wrapper.orderByDesc(Publish::getCreateTime);

        // 4. 执行分页查询
        Page<Publish> page = this.page(param.page(), wrapper);

        return new PageResult<>(page.getPages(), page.getRecords());
    }

    @Transactional
    @Override
    public void process(Long id, Integer isAccept) {
        Publish publish = DbUtil.checkNotFound(id, this);
        if (isAccept == 0) {
            publish.setStatus(0);
            publish.setValuation(new BigDecimal("0"));
        } else {
            publish.setStatus(2);
            orderService.payment(publish);
        }
        updateById(publish);
    }

    @Override
    public void valuation(PublishValuationVo vo) {
        Publish publish = DbUtil.checkNotFound(vo.getId(), this);
        publish.setValuation(vo.getValuation())
                .setDriverId(UserHolder.getId())
                .setStatus(1);
        updateById(publish);
    }

    @Override
    public Publish findById(Long id) {
        return DbUtil.checkNotFound(id, this);
    }
}
