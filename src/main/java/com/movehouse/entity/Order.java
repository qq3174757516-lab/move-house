package com.movehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Accessors(chain = true)
@TableName("orders")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String orderNo;

    private Long userId;

    private String username;

    private Long driverId;

    private String driverName;

    private BigDecimal price;

    //0未完成, 1已到达, 2已完成
    private Integer status;

    private String address;

    private Date createTime;

    private Date arriveTime;

    private Date finishTime;

    @TableField(exist = false)
    private Boolean isComplaint;
}
