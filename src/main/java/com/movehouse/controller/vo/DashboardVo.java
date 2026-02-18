package com.movehouse.controller.vo;

import lombok.Data;
import java.util.List;

@Data
public class DashboardVo {
    private Integer userCount;      // 总用户数
    private Integer driverCount;    // 司机人数
    private Integer orderCount;     // 总订单数
    private Integer carCount;       // 车辆总数

    // 图表数据
    private List<String> chartDates;  // 近7天日期，例如 ["04-10", "04-11"...]
    private List<Long> chartData;     // 对应日期的订单数，例如 [5, 12...]
}