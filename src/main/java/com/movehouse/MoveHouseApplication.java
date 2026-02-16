package com.movehouse;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.movehouse.mapper")
public class MoveHouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoveHouseApplication.class, args);
    }

}
