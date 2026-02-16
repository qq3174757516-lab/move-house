package com.movehouse.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movehouse.common.LoginUser;
import com.movehouse.controller.vo.LoginVo;
import com.movehouse.entity.Admin;
import com.movehouse.enums.UserTypeEnum;
import com.movehouse.mapper.AdminMapper;
import com.movehouse.service.AdminService;
import com.movehouse.util.TokenUtil;
import org.springframework.stereotype.Service;

import static cn.hutool.crypto.SecureUtil.md5;
import static com.movehouse.common.MoveHouseException.exception;
import static com.movehouse.common.MoveHouseRemind.USERNAME_OR_PASSWORD_ERROR;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Override
    public LoginUser login(LoginVo vo) {
        //校验用户名
        Admin admin = findByUsername(vo.getUsername());
        if (admin == null) {
            throw exception(USERNAME_OR_PASSWORD_ERROR);
        }

        //校验密码
        String password = md5(vo.getPassword());
        if (!StrUtil.equals(password, admin.getPassword())) {
            throw exception(USERNAME_OR_PASSWORD_ERROR);
        }

        //生成token并返回
        LoginUser loginUser = new LoginUser(admin.getId(), UserTypeEnum.ADMIN.getCode(), admin.getUsername(), null);
        TokenUtil.createToken(loginUser);
        return loginUser;
    }

    /**
     * 根据用户名查找管理员
     *
     * @param username 用户名
     * @return 返回
     */
    public Admin findByUsername(String username) {
        return lambdaQuery().eq(Admin::getUsername, username).one();
    }
}
