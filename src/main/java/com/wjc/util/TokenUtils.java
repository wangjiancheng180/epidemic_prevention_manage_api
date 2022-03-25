package com.wjc.util;

import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author 王建成
 * @date 2022/3/17--17:20
 */
@Component
@Lazy
@Data
public class TokenUtils implements Serializable {

    @Value("${token.header}")
    private String header;

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expireTime}")
    private int expireTime;

    /**
     * 获取Token
     * @param claims
     * @return
     */
    public String createToken(Map<String,Object>claims){
        //设置token过期时间（毫秒数）
        long now = System.currentTimeMillis()+(expireTime * 60 * 1000);
        return Jwts.builder()
                //签发者
                .setIssuer("wjc")
                .addClaims(claims)
                //设置过期时间
                .setExpiration(new Date(now))
                .signWith(SignatureAlgorithm.HS256,secret).compact();
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public Claims parseToken(String token){
        JwtParser jwtParser = Jwts.parser().setSigningKey(secret);
        Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return body;
    }


}
