package com.movehouse.controller;

import com.movehouse.annotation.PreAuthed;
import com.movehouse.common.LoginUser;
import com.movehouse.common.PageParam;
import com.movehouse.common.PageResult;
import com.movehouse.common.Result;
import com.movehouse.controller.vo.LoginVo;
import com.movehouse.controller.vo.RegisterVo;
import com.movehouse.entity.User;
import com.movehouse.service.UserService;
import com.movehouse.util.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

import static com.movehouse.enums.UserTypeEnum.USER;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户登录接口
     *
     * @param vo 登录Vo对象
     * @return 返回LoginUser对象
     */
    @PostMapping("/login")
    public Result<LoginUser> login(@RequestBody LoginVo vo) {
        LoginUser loginUser = userService.login(vo);
        return Result.success(loginUser);
    }

    /**
     * 用户注册接口
     *
     * @param vo 注册vo
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody RegisterVo vo) {
        userService.register(vo);
        return Result.success(true);
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 返回登录用户信息
     */
    @PreAuthed(USER)
    @GetMapping("/info")
    public Result<User> info() {
        User user = userService.getById(UserHolder.getId());
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 获取用户分页
     *
     * @param param 分页参数
     * @return 返回分页结果
     */
    @PreAuthed
    @GetMapping("/page")
    public Result<PageResult<User>> page(PageParam param) {
        PageResult<User> result = userService.pageUser(param);
        return Result.success(result);
    }

    /**
     * 修改用户信息
     *
     * @param user 用户实体类
     * @return 返回操作结果
     */
    @PreAuthed(USER)
    @PutMapping
    public Result<Boolean> update(@RequestBody User user) {
        userService.edit(user);
        return Result.success(true);
    }

    /**
     * 管理员给用户充值
     */
    @PreAuthed // 默认管理员权限
    @PatchMapping("/recharge")
    public Result<Boolean> recharge(@RequestBody Map<String, Object> params) {
        // 从参数中获取 id 和 amount
        Long id = Long.valueOf(params.get("id").toString());
        BigDecimal amount = new BigDecimal(params.get("amount").toString());

        userService.recharge(id, amount);
        return Result.success(true);
    }

}
