package com.movehouse.util;


import com.movehouse.common.LoginUser;
import com.movehouse.enums.UserTypeEnum;

public class UserHolder {
    private static final ThreadLocal<LoginUser> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(LoginUser user) {
        THREAD_LOCAL.set(user);
    }

    public static LoginUser get() {
        return THREAD_LOCAL.get();
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    public static Long getId() {
        LoginUser loginUser = THREAD_LOCAL.get();
        return loginUser == null ? null : loginUser.getId();
    }

    public static UserTypeEnum getUserType() {
        LoginUser loginUser = get();
        int type = loginUser.getUserType();
        return UserTypeEnum.valueOf(type);
    }

    public static boolean isCurrentUser(Long id) {
        LoginUser loginUser = get();
        return loginUser != null && loginUser.getId().equals(id);
    }
}
