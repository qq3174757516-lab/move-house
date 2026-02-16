package com.movehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.movehouse.common.PageParam;
import com.movehouse.common.PageResult;
import com.movehouse.entity.Car;


public interface CarService extends IService<Car> {

    void create(Car car);

    void delete(Long id);

    PageResult<Car> pageCar(PageParam param);
}
