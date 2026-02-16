package com.movehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@Accessors(chain = true)
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private Long carId;

    @TableField(exist = false)
    private Car car;

    private String idCard;

    private String avatar;

    private String name;

    private String phone;

    private String password;

    private Integer gender;

    private Integer age;

    private Integer carAge;

    private BigDecimal income;

    private Integer complaintNum;

    //0待审核, 1正常, 2解雇
    private Integer status;
}
