package com.movehouse.common;

/**
 * 公共异常类
 **/
public class MoveHouseException extends RuntimeException {
    private final int code;

    public MoveHouseException(MoveHouseRemind moveHouseRemindType) {
        super(moveHouseRemindType.getMessage());
        this.code = moveHouseRemindType.getCode();
    }

    public MoveHouseException(int code, String message) {
        super(message);
        this.code = code;
    }

    @Override
    public String toString() {
        return "MoveHouseException{" +
                "code=" + this.getCode() +
                ", message=" + this.getMessage() +
                '}';
    }

    public Integer getCode() {
        return code;
    }

    public static MoveHouseException exception(MoveHouseRemind moveHouseRemindType) {
        return new MoveHouseException(moveHouseRemindType);
    }

    public static MoveHouseException exception(int code, String message) {
        return new MoveHouseException(code, message);
    }
}
