package com.movehouse;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.builder.Service;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class GenerateCodeTest {
    @Test
    public void generateCode() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3001/dormitory", "root", "20030519")
                .globalConfig(builder -> {
                    builder.outputDir("src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.dormitory") // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.controllerBuilder().enableRestStyle();
                    Service.Builder serviceBuilder = builder.serviceBuilder();
                    //去掉I前缀
                    serviceBuilder.formatServiceFileName("%sService").formatServiceImplFileName("%sServiceImpl");
                    Entity.Builder entityBuilder = builder.build().entityBuilder();
                    entityBuilder.enableLombok().enableChainModel();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
