package com.movehouse.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.movehouse.common.PageParam;
import com.movehouse.common.PageResult;
import com.movehouse.entity.Car;
import com.movehouse.entity.Driver;
import com.movehouse.mapper.CarMapper;
import com.movehouse.service.CarService;
import com.movehouse.service.DriverService;
import com.movehouse.util.DbUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;



import static com.movehouse.common.MoveHouseException.exception;


@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {
    @Resource
    private DriverService driverService;

    @Override
    public void create(Car car) {
        //检查车牌号是否已经存在
        DbUtil.checkFieldRepeat("car_no", car.getCarNo(), this);
        save(car);
    }

    @Override
    public void delete(Long id) {
        //判断车辆是否已经被司机使用，如果正在使用则不能删除
        boolean exists = driverService.lambdaQuery().eq(Driver::getCarId, id).exists();
        if (exists) {
            throw exception(500, "车辆正在被司机使用!");
        }
        removeById(id);
    }

    @Override
    public PageResult<Car> pageCar(PageParam param) {
// 根据参数查询
        String keyword = param.getKeyword();
        // [新增] 获取类型参数
        String type = param.getType();

        Page<Car> page = lambdaQuery()
                //关键字模糊查询名称
                .like(StrUtil.isNotBlank(keyword), Car::getName, keyword)
                // 逻辑：如果 type 不为空，则精确匹配车辆类型
                .eq(StrUtil.isNotBlank(type), Car::getType, type)
                .page(param.page());

        return new PageResult<>(page.getTotal(), page.getRecords());
    }
}
