package com.movehouse.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.movehouse.common.MoveHouseException;
import com.movehouse.common.MoveHouseRemind;

import java.util.HashMap;
import java.util.Map;


public class DbUtil {
    public static <T> T checkNotFound(Long id, IService<T> service) {
        T t = service.getById(id);
        if (t == null) {
            throw new MoveHouseException(MoveHouseRemind.NOT_FOUND);
        }
        return t;
    }

    public static <T> void checkFieldRepeat(String column, String value, IService<T> service) {
        Map<String, Object> map = new HashMap<>();
        map.put(column, value);
        checkFieldRepeat(map, service);
    }

    public static <T> void checkFieldRepeat(Map<String, Object> map, IService<T> service) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            queryWrapper.eq(entry.getKey(), entry.getValue());
        }
        T one = service.getOne(queryWrapper);
        if (one != null) {
            throw new MoveHouseException(MoveHouseRemind.RESOURCE_EXISTS);
        }
    }
}
