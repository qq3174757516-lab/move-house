package com.movehouse.aop;

import cn.hutool.core.util.ArrayUtil;
import com.movehouse.annotation.PreAuthed;
import com.movehouse.common.LoginUser;
import com.movehouse.common.MoveHouseRemind;
import com.movehouse.entity.Driver;
import com.movehouse.service.DriverService;
import com.movehouse.util.UserHolder;
import jakarta.annotation.Resource;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import static com.movehouse.common.MoveHouseException.exception;
import static com.movehouse.common.MoveHouseRemind.USER_TYPE_ERROR;
import static com.movehouse.enums.UserTypeEnum.DRIVER;


@Aspect
@Component
public class PreAuthedAspect {
    @Resource
    private DriverService driverService;

    /**
     * 用于校验身份信息的切面, 如果方法上有这个注解就表示一定要登录
     * preAuthed的value代表访问接口是以什么身份
     * common=true表示需要登录, 但是不进行身份的校验
     *
     * @param preAuthed 注解@PreAuthed
     */
    @Before("@annotation(preAuthed)")
    public void before(PreAuthed preAuthed) {
        LoginUser loginUser = UserHolder.get();
        if (loginUser == null) {
            throw exception(MoveHouseRemind.NOT_AUTHORIZE);
        }

        if (UserHolder.getUserType().equals(DRIVER)) {
            boolean exists = driverService.lambdaQuery()
                    .eq(Driver::getId, loginUser.getId())
                    .eq(Driver::getStatus, 2)
                    .exists();
            if (exists) {
                throw exception(MoveHouseRemind.NOT_AUTHORIZE);
            }
        }

        //如果是通用接口, 不校验身份
        if (preAuthed.common()) {
            return;
        }

        if (!ArrayUtil.contains(preAuthed.value(), UserHolder.getUserType())) {
            throw exception(USER_TYPE_ERROR);
        }
    }
}
