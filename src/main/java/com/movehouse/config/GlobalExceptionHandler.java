package com.movehouse.config;

import com.movehouse.common.MoveHouseException;
import com.movehouse.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常拦截
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MoveHouseException.class)
    public Result<Object> houseException(MoveHouseException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }
}
