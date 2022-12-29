package com.xsx.jsoup.common.util;

import com.alibaba.fastjson.JSON;
import com.xsx.jsoup.config.JwtProperties;
import com.xsx.jsoup.entity.dto.UserTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @Author:夏世雄
 * @Date: 2022/10/31/17:48
 * @Version: 1.0
 * @Discription:
 **/
@Slf4j
@Component
public class JwtUtil {


    private static final String ISSUER = "SMAPET_ACCOUNT";
    private JwtProperties jwtProperties;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String createToken(UserTokenDTO account) {
        Date expireTime = new Date(System.currentTimeMillis() + jwtProperties.getExpireTime() * 1000);
        String token = "Bearer" + " ";
        try {
            token += JWT.create().withIssuer(ISSUER)
                    .withClaim("id", account.getId())
                    .withClaim("name", account.getName())
                    .withClaim("mobile", account.getMobile())
                    .withExpiresAt(expireTime)
                    .sign(Algorithm.HMAC256(jwtProperties.getSecret()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("创建token失败!");
        }
        return token;
    }

    public void checkToken(UserTokenDTO account, String jwtToken) {
        if (account == null) {
            throw new RuntimeException("account为空!");
        }
        Map<String, Object> result;
        try {
            result = parseToken(jwtToken);
        } catch (Exception ex) {
            throw new RuntimeException("解析token失败!" + ex.getMessage());
        }
        String id = (String) result.get("id");
        if (!id.equals(account.getId())) {
            log.warn("用户Id和解析token的用户ID不匹配! {}", jwtToken);
            throw new RuntimeException("非法token!");
        }
        String mobile = (String) result.get("mobile");
        if (!mobile.equals(account.getMobile())) {
            log.warn("用户mobile和解析token的用户mobile不匹配! {}", jwtToken);
            throw new RuntimeException("非法token!");
        }
        Date expiresAt = (Date) result.get("expiresAt");
        if (expiresAt == null) {
            log.warn("非法token, 缺少exp(expiresAt)属性! {}", jwtToken);
            throw new RuntimeException("非法token!");
        }
        //验证token过期时间
        Date now = new Date();
        if (now.after(expiresAt)) {
            log.warn("token已过期！");
            throw new RuntimeException("token已过期!");
        }
    }

    public Map<String, Object> parseToken(String jwtToken) {
        if (org.apache.commons.lang3.StringUtils.isBlank(jwtToken)) {
            throw new RuntimeException("token为空!");
        }
        String token = jwtToken.trim();
        String[] tokenArray = StringUtils.splitByWholeSeparator(token, " ");
        if (tokenArray.length > 2) {
            throw new RuntimeException("token格式错误");
        }
        if (tokenArray.length == 2) {
            if (!tokenArray[0].equals("Bearer")) {
                throw new RuntimeException("token格式错误");
            }
            token = tokenArray[1];
        }

        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtProperties.getSecret())).withIssuer(ISSUER).build();
            DecodedJWT jwt = verifier.verify(token);
            Long id = jwt.getClaim("id").asLong();
            String name = jwt.getClaim("name").asString();
            String mobile = jwt.getClaim("mobile").asString();
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("mobile", mobile);
            map.put("expiresAt", jwt.getExpiresAt());
            return map;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("解析token失败!");
        }
    }

    public UserTokenDTO parseToken1(String jwtToken) {
        UserTokenDTO userTokenDTO = new UserTokenDTO();
        Map<String, Object> map = this.parseToken(jwtToken);
        if (!CollectionUtils.isEmpty(map)) {
            userTokenDTO.setId((long) map.get("id"));
            userTokenDTO.setName(map.get("name").toString());
            userTokenDTO.setMobile(map.get("mobile").toString());
        }
        return userTokenDTO;
    }

    public UserTokenDTO parseToken2(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        DecodedJWT jwt = verifier.verify(token);
        String result = jwt.getClaim("token").asString();
        return JSON.parseObject(result, UserTokenDTO.class);
    }


    public String generateToken(UserTokenDTO userTokenDTO) {
        try {
            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withHeader(header)
                    .withClaim("token", JSON.toJSONString(userTokenDTO))
                    //不在此处设置token过期时间，使用redis设置过期时间
                    //.withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("generate token occur error, error is:{}", e);
            return null;
        }
    }
}
