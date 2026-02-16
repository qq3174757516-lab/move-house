package com.movehouse.controller.vo;

import lombok.Data;

@Data
public class DriverRegisterVo {
    private String idCard;
    private String username;
    private String name;
    private String phone;
    private String password;
    private Integer gender;
    private Integer age;
    private Integer carAge;
}
