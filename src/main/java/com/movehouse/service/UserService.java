package com.movehouse.service;

import com.movehouse.common.LoginUser;
import com.movehouse.common.PageParam;
import com.movehouse.common.PageResult;
import com.movehouse.controller.vo.LoginVo;
import com.movehouse.controller.vo.RegisterVo;
import com.movehouse.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {

    LoginUser login(LoginVo vo);

    void register(RegisterVo vo);

    void edit(User user);

    PageResult<User> pageUser(PageParam queryVo);
}
