package com.movehouse.controller.vo;

import com.movehouse.common.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class ComplaintQueryVo extends PageParam {
    private Long driverId;
}
