package com.example.level04.jwt;

import com.example.level04.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer";

    private final long TOKEN_TIME = 60 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init(){
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(String username, UserRoleEnum role){
        log.info("토큰 만들기");
        Date date = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setSubject(username)
                .claim(AUTHORIZATION_KEY, role)
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key,signatureAlgorithm)
                .compact();
    }


    // header에서 jwt 가져오기
    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }
        return null;
    }

//    public String substringToken(String tokenValue){
//        log.info("토큰 자르기");
//        if(StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)){
//            return tokenValue.substring(7);
//        }
//        logger.error("토큰 발견 안됨");
//        throw new NullPointerException("토큰 없다구요");
//    }

    public boolean validateToken(String token){
        log.info("토큰 상태 검사 " + token);
        try {
            log.info("validateToken try 시작");
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (SecurityException | MalformedJwtException | SignatureException e){
            logger.error("유효하지 않는 JWT 서명");
        }catch (ExpiredJwtException e){
            logger.error("토큰 만료됨요");
        }catch (UnsupportedJwtException e){
            logger.error("지원되지않는 JWT토큰");
        }catch (IllegalArgumentException e){
            logger.error("잘못된 JWT토큰");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token){
        log.info("유저 정보 가져오기");
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getTokenFromRequest(HttpServletRequest request){
        log.info("요청에서 토큰가져오기");
        Cookie[] cookies = request.getCookies();
        System.out.println("cookies = " + Arrays.toString(cookies));
        if(cookies != null){
            for (Cookie cookie : cookies){
                if(cookie.getName().equals(AUTHORIZATION_HEADER)){
                    try {
                        return URLDecoder.decode(cookie.getValue(),"UTF-8");
                    }catch (UnsupportedEncodingException e){
                        return null;
                    }
                }
            }
        }
        return null;
    }
}

