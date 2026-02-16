package com.movehouse.util;


import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTException;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.movehouse.common.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class TokenUtil {

    private static JWTSigner jwtSigner;
    private static Integer expireHours;

    public TokenUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expire-hours}") Integer expireHours) {
        jwtSigner = JWTSignerUtil.hs256(secret.getBytes());
        TokenUtil.expireHours = expireHours;
    }

    public static void createToken(LoginUser loginUser) {
        Date now = new Date();
        String token = JWT.create()
                .setPayload("id", loginUser.getId())
                .setPayload("userType", loginUser.getUserType())
                .setPayload("username", loginUser.getUsername())
                .setExpiresAt(DateUtil.offsetHour(now, expireHours))
                .setIssuedAt(now)
                .setNotBefore(now)
                .sign(jwtSigner);
        loginUser.setToken(token);
    }

    public static LoginUser parseToken(String token) {
        try {
            if (token == null) return null;
//            if (!JWTUtil.verify(token, jwtSigner)) {
//                log.info("JWTUtil verify token: {} failed！", token);
//                return null;
//            }

            JWT jwt = JWTUtil.parseToken(token);
            JSONObject payloads = jwt.getPayloads();

            Date exp = payloads.getDate("exp");
            System.out.println(exp);
            if (exp.before(new Date())) {
                log.info("JWTUtil verify token: {} expired！", token);
                return null;
            }

            return new LoginUser(payloads.getLong("id"), payloads.getInt("userType"), payloads.getStr("username"), token);
        } catch (JWTException jwtException) {
            log.error("parseToken failed token: {}", token);
        }

        return null;
    }

    public static boolean verifyToken(String token) {
        if (!JWTUtil.verify(token, jwtSigner)) return false;

        JWT jwt = JWTUtil.parseToken(token);
        JSONObject payloads = jwt.getPayloads();

        Date exp = payloads.getDate("exp");
        return exp.before(new Date());
    }
}
