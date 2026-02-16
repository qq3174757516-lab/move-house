package com.movehouse.service;

import com.movehouse.common.LoginUser;
import com.movehouse.controller.vo.LoginVo;
import com.movehouse.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;


public interface AdminService extends IService<Admin> {

    LoginUser login(LoginVo vo);
}
