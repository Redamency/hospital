package com.shu.hospital.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Young
 * @date: 2023/2/19 22:35
 * @email: 1683209437@qq.com
 */
public class JWTUtils {
    private static final String jwtToken = "Young";

    public static String createToken(Long userId) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId",userId);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtToken)
                .setClaims(claims)
                .setIssuedAt(new Date())    //设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 24L * 60 * 60 * 60 * 1000));    //一天
        String token = jwtBuilder.compact();
        return token;
    }

    public static Map<String, Object> checkToken(String token) {
        try {
            Jwt parse = (Jwts.parserBuilder().setSigningKey(jwtToken)).build().parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String token = JWTUtils.createToken(100L);
        System.out.println(token);
        Map<String, Object> map = JWTUtils.checkToken(token);
        System.out.println(map.get("userId"));

    }

}