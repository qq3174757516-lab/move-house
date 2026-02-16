package com.movehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.movehouse.common.LoginUser;
import com.movehouse.common.PageResult;
import com.movehouse.controller.vo.DriverQueryVo;
import com.movehouse.controller.vo.DriverRegisterVo;
import com.movehouse.controller.vo.LoginVo;
import com.movehouse.entity.Driver;


public interface DriverService extends IService<Driver> {

    LoginUser login(LoginVo vo);

    void register(DriverRegisterVo vo);

    PageResult<Driver> pageDriver(DriverQueryVo vo);

    void edit(Driver driver);

    void accept(Long id, Long carId);

    void dismiss(Long id);
}
