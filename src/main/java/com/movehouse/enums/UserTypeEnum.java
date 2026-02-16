package com.movehouse.enums;

import cn.hutool.core.util.ArrayUtil;
import com.movehouse.common.MoveHouseException;
import com.movehouse.common.MoveHouseRemind;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {
    ADMIN(1), //管理员
    DRIVER(2), //司机
    USER(3), //用户
    ;
    private final int code;

    public static UserTypeEnum valueOf(int code) {
        UserTypeEnum userTypeEnum = ArrayUtil.firstMatch(e -> e.getCode() == code, values());
        if (userTypeEnum == null) {
            throw new MoveHouseException(MoveHouseRemind.NOT_FOUND);
        }
        return userTypeEnum;
    }
}
