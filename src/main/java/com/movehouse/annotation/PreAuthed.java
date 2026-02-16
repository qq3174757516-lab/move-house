package com.movehouse.annotation;


import com.movehouse.enums.UserTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.movehouse.enums.UserTypeEnum.ADMIN;

/**
 * 切面拦截，判断用户是否有权限访问对应的接口
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreAuthed {
    UserTypeEnum[] value() default ADMIN;

    boolean common() default false;
}
