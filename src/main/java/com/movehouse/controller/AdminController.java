package com.movehouse.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.movehouse.common.LoginUser;
import com.movehouse.common.Result;
import com.movehouse.controller.vo.DashboardVo;
import com.movehouse.controller.vo.LoginVo;
import com.movehouse.entity.Order;
import com.movehouse.service.*;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CarService carService;

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

    /**
     * 获取首页统计数据
     */
    @GetMapping("/stats")
    public Result<DashboardVo> getDashboardStats() {
        DashboardVo vo = new DashboardVo();

        // 1. 获取基础统计
        vo.setUserCount((int) userService.count());
        vo.setDriverCount((int) driverService.count());
        vo.setOrderCount((int) orderService.count());
        vo.setCarCount((int) carService.count());

        // 2. [新增] 计算近7天的订单趋势图表数据
        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();

        // 循环过去7天
        for (int i = 6; i >= 0; i--) {
            // 获取那一天的时间范围
            Date date = DateUtil.offsetDay(new Date(), -i);
            String dateStr = DateUtil.format(date, "MM-dd"); // X轴显示格式

            // 构建查询：当天 00:00:00 到 23:59:59
            Date beginOfDay = DateUtil.beginOfDay(date);
            Date endOfDay = DateUtil.endOfDay(date);

            long count = orderService.count(new QueryWrapper<Order>()
                    .between("create_time", beginOfDay, endOfDay));

            dates.add(dateStr);
            counts.add(count);
        }

        vo.setChartDates(dates);
        vo.setChartData(counts);

        return Result.success(vo);
    }


}
