package com.movehouse.common;

import lombok.Data;

/**
 * 统一结果返回
 *
 * @param <T>
 */
@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功!", data);
    }

    public static <T> Result<T> fail(MoveHouseRemind moveHouseRemindType) {
        return new Result<>(moveHouseRemindType.getCode(), moveHouseRemindType.getMessage(), null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }
}
