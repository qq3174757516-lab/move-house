package com.movehouse.controller;

import com.movehouse.annotation.PreAuthed;
import com.movehouse.common.LoginUser;
import com.movehouse.common.PageResult;
import com.movehouse.common.Result;
import com.movehouse.controller.vo.DriverQueryVo;
import com.movehouse.controller.vo.DriverRegisterVo;
import com.movehouse.controller.vo.LoginVo;
import com.movehouse.entity.Driver;
import com.movehouse.service.DriverService;
import com.movehouse.util.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.movehouse.enums.UserTypeEnum.DRIVER;

@RestController
@RequestMapping("/driver")
public class DriverController {
    @Resource
    private DriverService driverService;

    //TODO 前端页面中司机列表及以下的工能还未测试,用户端和司机端还未开始测试并且需要美化和新增功能

    /**
     * 司机登录接口
     *
     * @param vo 登录Vo对象
     * @return 返回LoginUser对象
     */
    @PostMapping("/login")
    public Result<LoginUser> login(@RequestBody LoginVo vo) {
        LoginUser loginUser = driverService.login(vo);
        return Result.success(loginUser);
    }

    /**
     * 司机注册
     *
     * @param vo 司机注册vo
     * @return 返回注册结果
     */
    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody DriverRegisterVo vo) {
        driverService.register(vo);
        return Result.success(true);
    }

    /**
     * 查询当前登录的司机的基本信息
     *
     * @return 返回当前登录司机的信息
     */
    @PreAuthed(DRIVER)
    @GetMapping("/info")
    public Result<Driver> info() {
        Driver driver = driverService.getById(UserHolder.getId());
        driver.setPassword(null);
        return Result.success(driver);
    }

    /**
     * 查询司机分页信息
     *
     * @param vo 查询对象
     * @return 返回分页结果
     */
    @PreAuthed
    @GetMapping("/page")
    public Result<PageResult<Driver>> page(DriverQueryVo vo) {
        PageResult<Driver> result = driverService.pageDriver(vo);
        return Result.success(result);
    }

    /**
     * 查询司机列表
     *
     * @return 返回司机列表
     */
    @PreAuthed
    @GetMapping
    public Result<List<Driver>> list() {
        return Result.success(driverService.list());
    }

    /**
     * 分配车辆给司机
     *
     * @param id    司机id
     * @param carId 要分配给司机的车辆id
     * @return 返回操作结果
     */
    @PreAuthed
    @PatchMapping("/accept/{id}/{carId}")
    public Result<Boolean> accept(@PathVariable Long id, @PathVariable Long carId) {
        driverService.accept(id, carId);
        return Result.success(true);
    }

    /**
     * 解雇司机
     *
     * @param id 司机id
     * @return 返回操作结果
     */
    @PreAuthed
    @PatchMapping("/dismiss/{id}")
    public Result<Boolean> dismiss(@PathVariable Long id) {
        driverService.dismiss(id);
        return Result.success(true);
    }

    /**
     * 删除司机
     *
     * @param id 司机id
     * @return 返回操作结果
     */
    @PreAuthed
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        driverService.removeById(id);
        return Result.success(true);
    }

    /**
     * 修改司机信息
     *
     * @param driver 司机实体类
     * @return 返回操作结果
     */
    @PreAuthed(DRIVER)
    @PutMapping
    public Result<Boolean> update(@RequestBody Driver driver) {
        driverService.edit(driver);
        return Result.success(true);
    }
}
