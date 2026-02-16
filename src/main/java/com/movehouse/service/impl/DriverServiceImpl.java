package com.movehouse.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movehouse.common.LoginUser;
import com.movehouse.common.PageResult;
import com.movehouse.controller.vo.DriverQueryVo;
import com.movehouse.controller.vo.DriverRegisterVo;
import com.movehouse.controller.vo.LoginVo;
import com.movehouse.entity.Car;
import com.movehouse.entity.Driver;
import com.movehouse.entity.Order;
import com.movehouse.entity.Publish;
import com.movehouse.mapper.DriverMapper;
import com.movehouse.service.CarService;
import com.movehouse.service.DriverService;
import com.movehouse.service.OrderService;
import com.movehouse.service.PublishService;
import com.movehouse.util.DbUtil;
import com.movehouse.util.TokenUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static cn.hutool.crypto.SecureUtil.md5;
import static com.movehouse.common.MoveHouseException.exception;
import static com.movehouse.common.MoveHouseRemind.USERNAME_OR_PASSWORD_ERROR;
import static com.movehouse.enums.UserTypeEnum.DRIVER;


@Service
public class DriverServiceImpl extends ServiceImpl<DriverMapper, Driver> implements DriverService {
    @Resource
    private CarService carService;
    @Resource
    private OrderService orderService;
    @Resource
    private PublishService publishService;

    @Override
    public LoginUser login(LoginVo vo) {
        String username = vo.getUsername();

        Driver driver = findByUsername(username);
        //校验用户名
        if (driver == null) {
            driver = findByPhone(username);
        }
        if (driver == null) {
            throw exception(USERNAME_OR_PASSWORD_ERROR);
        }

        //校验密码
        String password = md5(vo.getPassword());
        if (!StrUtil.equals(password, driver.getPassword())) {
            throw exception(USERNAME_OR_PASSWORD_ERROR);
        }

        //校验状态
        if (Objects.equals(driver.getStatus(), 0)) {
            throw exception(500, "账号待审核!");
        }
        if (Objects.equals(driver.getStatus(), 2)) {
            throw exception(500, "你已被解雇!");
        }

        //生成token并返回
        LoginUser loginUser = new LoginUser(driver.getId(), DRIVER.getCode(), driver.getPhone(), null);
        TokenUtil.createToken(loginUser);
        return loginUser;
    }

    @Override
    public void register(DriverRegisterVo vo) {
        Driver driver = BeanUtil.copyProperties(vo, Driver.class);
        driver.setPassword(md5(driver.getPassword()));
        save(driver);
    }

    @Override
    public PageResult<Driver> pageDriver(DriverQueryVo vo) {
        Integer status = vo.getStatus();
        Integer gender = vo.getGender();
        String keyword = vo.getKeyword();

        Page<Driver> page = lambdaQuery().and(StrUtil.isNotBlank(keyword), e -> e
                        .like(Driver::getName, keyword).or()
                        .like(Driver::getPhone, keyword))
                .eq(status != null, Driver::getStatus, status)
                .eq(gender != null, Driver::getGender, gender)
                .page(vo.page());

        page.getRecords().forEach(e -> e.setCar(carService.getById(e.getCarId())));

        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    @Override
    public void edit(Driver driver) {
        if (StrUtil.isNotBlank(driver.getPassword())) {
            driver.setPassword(md5(driver.getPassword()));
        }
        updateById(driver);
    }

    @Transactional
    @Override
    public void accept(Long id, Long carId) {
        Driver driver = DbUtil.checkNotFound(id, this);
        Car car = DbUtil.checkNotFound(carId, carService);
        driver.setCarId(carId).setStatus(1);
        car.setStatus(1);
        updateById(driver);
        carService.updateById(car);
    }

    @Transactional
    @Override
    public void dismiss(Long id) {
        boolean exists = orderService.lambdaQuery()
                .eq(Order::getDriverId, id)
                .eq(Order::getStatus, 0)
                .exists();
        if (exists) {
            throw exception(500, "司机还有未完成的订单, 不可解雇!");
        }

        //解除司机正在交涉的发布信息
        publishService.lambdaUpdate()
                .eq(Publish::getDriverId, id)
                .eq(Publish::getStatus, 1)
                .set(Publish::getStatus, 0)
                .set(Publish::getValuation, 0)
                .update();

        Driver driver = DbUtil.checkNotFound(id, this);
        Car car = DbUtil.checkNotFound(driver.getCarId(), carService);
        driver.setCarId(0L).setStatus(2);
        car.setStatus(0);
        updateById(driver);
        carService.updateById(car);
    }

    //根据手机号查询司机
    public Driver findByPhone(String phone) {
        return lambdaQuery().eq(Driver::getPhone, phone).one();
    }

    //根据用户名查询司机
    public Driver findByUsername(String username) {
        return lambdaQuery().eq(Driver::getUsername, username).one();
    }

}
