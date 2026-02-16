package com.movehouse.controller;

import com.movehouse.annotation.PreAuthed;
import com.movehouse.common.PageParam;
import com.movehouse.common.PageResult;
import com.movehouse.common.Result;
import com.movehouse.entity.Order;
import com.movehouse.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import static com.movehouse.enums.UserTypeEnum.USER;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    private OrderService orderService;

    /**
     * 查询订单分页
     *
     * @param param 分页参数
     * @return 分页结果
     */
    @PreAuthed(common = true)
    @GetMapping("/page")
    public Result<PageResult<Order>> page(PageParam param) {
        PageResult<Order> result = orderService.pageOrder(param);
        return Result.success(result);
    }

    /**
     * 用户修改订单状态, 0代表司机已到达, 1代表订单已完成
     *
     * @param id     订单id
     * @param status 状态0 or 1
     */
    @PreAuthed(USER)
    @PatchMapping("/change-status/{id}/{status}")
    public Result<Boolean> changeStatus(@PathVariable Long id, @PathVariable Integer status) {
        orderService.changeStatus(id, status);
        return Result.success(true);
    }
}
