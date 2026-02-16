package com.movehouse;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Map;

@SpringBootTest
@Slf4j
class MoveHouseApplicationTests {

    @Test
    void contextLoads() {
        Map<String, Object> map = MapUtil.<String, Object>builder()
                .put("name", "shangsan")
                .put("age", 18)
                .build();
        JWTSigner jwtSigner = JWTSignerUtil.hs256("abc".getBytes());
        JWTSigner jwtSigner1 = JWTSignerUtil.hs256("abc".getBytes());

        String token = JWTUtil.createToken(map, jwtSigner);

        Date now = new Date();
        token = JWT.create()
                .setPayload("name", "shangsan")
                .setPayload("age", 18)
                .setKey("abc".getBytes())
                .setExpiresAt(DateUtil.offsetMinute(now, 1))
                .setIssuedAt(now)
                .setNotBefore(now)
                .sign(jwtSigner);

        log.info("token: {}", token);

        String t = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoic2hhbmdzYW4iLCJhZ2UiOjE4LCJleHAiOjE3NDQxODY1MTIsImlhdCI6MTc0NDE4NjQ1MiwibmJmIjoxNzQ0MTg2NDUyfQ.VF1_3kQ6_dnP-4PQeMGCsB49GHHsodRUq7FPsSy8-pc";

        System.out.println(JWTUtil.verify(t, jwtSigner1));

        JWT jwt = JWTUtil.parseToken(token);
        System.out.println(jwt.getPayloads().getStr("name"));
        System.out.println(jwt.getPayloads().getDate("exp"));
        System.out.println(jwt.getPayloads().getDate("nbf"));
    }
}
