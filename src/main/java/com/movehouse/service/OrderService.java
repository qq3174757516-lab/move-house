package com.movehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.movehouse.common.PageParam;
import com.movehouse.common.PageResult;
import com.movehouse.entity.Order;
import com.movehouse.entity.Publish;


public interface OrderService extends IService<Order> {
    void payment(Publish publish);

    PageResult<Order> pageOrder(PageParam param);

    void changeStatus(Long id, Integer status);
}
