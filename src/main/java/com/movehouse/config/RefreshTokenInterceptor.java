package com.movehouse.config;

import cn.hutool.core.util.StrUtil;
import com.movehouse.util.TokenUtil;
import com.movehouse.util.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;



/**
 * 刷新token拦截器
 */
@Component
@Slf4j
public class RefreshTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");
        if (StrUtil.isNotEmpty(token)) {
            log.info("RefreshTokenInterceptor token: {}, path: {}", token, request.getRequestURI());
            UserHolder.set(TokenUtil.parseToken(token));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserHolder.remove();
    }
}
