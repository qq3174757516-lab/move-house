package com.movehouse.controller.vo;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class PublishValuationVo {
    private Long id;
    private BigDecimal valuation;
}
