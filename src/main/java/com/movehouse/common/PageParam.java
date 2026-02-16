package com.movehouse.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页查询基本参数类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageParam {
    private Integer current = 1;
    private Integer size = 10;
    private String keyword;

    public <T> Page<T> page() {
        return new Page<>(current, size);
    }
}
