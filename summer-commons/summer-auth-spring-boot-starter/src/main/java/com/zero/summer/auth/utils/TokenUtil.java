package com.zero.summer.auth.utils;

import com.zero.summer.core.constant.SecurityConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Zero.
 * @date 2023/2/16 10:27 PM
 */
public class TokenUtil {

    /**
     * 根据用户信息,生成Jwt令牌.
     * @param claims    自定义数据载体
     * @param username  令牌所属用户的用户名
     * @return          Token
     */
    public static String generateToken(Map<String,Object> claims,String username){
        return Jwts.builder()
                .setClaims(claims) //设置自定义扩展数据
                .setSubject(username)//设置用户名
                .setIssuedAt(new Date(System.currentTimeMillis())) //创建令牌时间
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRED_TIME)) //令牌到期时间
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();    //设置盐值和算法类型
    }
    public static String generateToken(Map<String,Object> claims, UserDetails userDetails){
        return generateToken(claims,userDetails.getUsername());
    }
    public static String generateToken(UserDetails userDetails){
        return generateToken(userDetails.getUsername());
    }
    public static String generateToken(String username){
        return generateToken(new HashMap<>(),username);
    }

    /**
     * 获取token的username
     * @param token 令牌
     * @return      用户名
     */
    public static String getUsername(String token){
        return getClaimsVal(token,Claims::getSubject);
    }


    /**
     * 将 Token解析为Claims
     * @param token 要解析的Token
     * @return      解析后的Claims
     */
    public static Claims parseToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getKey()).build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 根据Token解析为 Claims,并且获取Claims的属性
     * @param token     令牌
     * @param function  要获取的Claims属性函数
     * @return          Claims的属性值
     */
    private static  <T> T getClaimsVal(String token, Function<Claims,T> function){
        return function.apply(parseToken(token));
    }


    /**
     * 校验令牌是否过期
     * @param token 令牌
     * @return  true: 已过期
     */
    public static boolean isTokenExpired(String token){
        Date expired = getClaimsVal(token, Claims::getExpiration);
        return expired.before(new Date());
    }

    /**
     * 校验令牌的用户名与指定用户是否相同,并且未过期
     * @param token         令牌
     * @param userDetails   用户
     * @return   true: 同一用户,并且未过期
     */
    public static boolean isTokenValid(String token,UserDetails userDetails){
        return (getUsername(token).equals(userDetails.getUsername())) &&
                !isTokenExpired(token);
    }


    /**
     * 将秘钥转换为{@link Key}
     */
    private static Key getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SecurityConstant.SECRET);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return key;
    }

    public static void main(String[] args) {
        String admin = TokenUtil.generateToken("admin");
        System.out.println(admin);
    }
}
