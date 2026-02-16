package com.movehouse.controller;

import com.movehouse.annotation.PreAuthed;
import com.movehouse.common.PageParam;
import com.movehouse.common.PageResult;
import com.movehouse.common.Result;
import com.movehouse.entity.Car;
import com.movehouse.service.CarService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {
    @Resource
    private CarService carService;

    /**
     * 添加车辆信息接口
     *
     * @param car 车辆实体类
     * @return 返回操作结果
     */
    @PreAuthed
    @PostMapping
    public Result<Boolean> create(@RequestBody Car car) {
        carService.create(car);
        return Result.success(true);
    }

    /**
     * 根据id删除车辆信息
     *
     * @param id id
     * @return 返回操作结果
     */
    @PreAuthed
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        carService.delete(id);
        return Result.success(true);
    }

    /**
     * 根据分页参数查询车辆信息
     *
     * @param param 分页参数
     * @return 返回分页结果
     */
    @PreAuthed
    @GetMapping("/page")
    public Result<PageResult<Car>> page(PageParam param) {
        PageResult<Car> pageResult = carService.pageCar(param);
        return Result.success(pageResult);
    }

    /**
     * 查询车辆信息列表
     *
     * @return 返回车辆信息列表
     */
    @PreAuthed
    @GetMapping
    public Result<List<Car>> list() {
        List<Car> list = carService.lambdaQuery()
                .eq(Car::getStatus, 0)
                .list();
        return Result.success(list);
    }
}
