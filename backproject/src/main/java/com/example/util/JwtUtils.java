package com.example.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtUtils {
    //Jwt秘钥
    @Value("${spring.security.key}")
    String key;

    @Value("${spring.security.expire}")
    int expire;


    public HashSet<String> blackList = new HashSet<>();

    //根据用户信息创建Jwt令牌
    public String createJwt(UserDetails user){
        Algorithm algorithm = Algorithm.HMAC256(key);
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.HOUR, expire);
        int userid = 18;
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withClaim("id", userid)
                .withClaim("name", user.getUsername())  //配置JWT自定义信息
                .withClaim("authorities", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withExpiresAt(calendar.getTime())  //设置过期时间
                .withIssuedAt(now)    //设置创建创建时间
                .sign(algorithm);   //最终签名
    }
    //加入黑名单方法
    public boolean invalidate(String token){
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT verify = jwtVerifier.verify(token);
            Map<String, Claim> claims = verify.getClaims();
            //取出UUID丢进黑名单中
            return blackList.add(verify.getId());
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public DecodedJWT resolveJwt(String headerToken){
        String token = this.convertToken(headerToken);
        if(token == null) return null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        try {
            DecodedJWT verify = jwtVerifier.verify(token);
            Date expiration = verify.getExpiresAt();
            return new Date().after(expiration) ? null : verify;
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public String convertToken(String headerToken){
        if(headerToken == null || !headerToken.startsWith("Bearer "))
            return null;
        return headerToken.substring(7);
    }

    public UserDetails toUser(DecodedJWT jwt) {
        Map<String, Claim> claims = jwt.getClaims();
        return User
                .withUsername(claims.get("name").asString())
                .password("******")
                .authorities(claims.get("authorities").asArray(String.class))
                .build();
    }

    public Date expireTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, expire);
        return calendar.getTime();
    }
}