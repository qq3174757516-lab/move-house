package com.movehouse.controller;

import com.movehouse.common.LoginUser;
import com.movehouse.common.Result;
import com.movehouse.controller.vo.LoginVo;
import com.movehouse.service.AdminService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;

    /**
     * 管理员登录
     *
     * @param vo 登录Vo对象
     * @return 返回LoginUser对象
     */
    @PostMapping("/login")
    public Result<LoginUser> login(@RequestBody LoginVo vo) {
        LoginUser loginUser = adminService.login(vo);
        return Result.success(loginUser);
    }
}
