package com.movehouse.common;

import lombok.Getter;

/**
 * 按照枚举定义系统内所有错误，统一管理错误
 */
@Getter
public enum MoveHouseRemind {
    USER_TYPE_ERROR(500, "用户类型错误!"),
    RESOURCE_EXISTS(501, "资源已存在!"),
    USERNAME_PASSWORD_ERROR(504, "用户名或密码错误!"),
    DOWNLOAD_FAILED(507, "下载失败!"),
    NOT_AUTHORIZE(401, "未登录!"),
    INVALID_ACCESS(403, "非法访问!"),
    NOT_FOUND(404, "资源不存在!"),
    USERNAME_OR_PASSWORD_ERROR(500, "用户名或密码错误!"),
    USER_EXISTS(500, "用户已存在!"),
    ;
    private final int code;
    private final String message;

    MoveHouseRemind(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
