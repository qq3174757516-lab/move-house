package com.movehouse.controller.vo;

import com.movehouse.common.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class DriverQueryVo extends PageParam {
    private Integer gender;
    private Integer status;
}
