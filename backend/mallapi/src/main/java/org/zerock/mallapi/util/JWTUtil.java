package org.zerock.mallapi.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import java.time.ZonedDateTime;
import java.util.*;
import javax.crypto.SecretKey;

@Log4j2
public class JWTUtil {
    private static String key = "1234567890123456789012345678901234567890";

    public static String generateToken(Map<String, Object> valueMap, int min) {
        // valueMap: 토큰에 담을 클레임(Claims) 데이터, key-value 형태의 정보들(예: 사용자 id, 권한 등)
        // min: 토큰 유효 기간

        SecretKey key = null;
        try {
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8")); // Keys.hmacShaKeyFor() 메서드는 이 바이트 배열을 가지고 SecretKey 객체
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        // jwt 토큰은 헤더, 페이로드, 서명으로 구분된다.
        String jwtStr = Jwts.builder()
                .setHeader(Map.of("typ", "JWT")) // typ: 토큰 타입, jwt종류
                .setClaims(valueMap) // 토큰 안에 담을 payload 정보를 클레임(Claims)으로 설정합니다.
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant())) // 토큰 발급 시각을 현재 시각으로 지정합니다.
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant())) // 토큰 만료 시간을 현재 시각 + min 분으로 지정합니다.
                .signWith(key) // 앞에서 만든 SecretKey로 토큰에 서명을 합니다. (기본은 HS256 알고리즘)
                .compact(); // 최종 JWT 문자열을 생성해서 반환합니다.
            // 서명이란? :키는 서명을 생성/검증하는 데 사용되는 별도의 비밀 정보
        return jwtStr;
    }

    public static Map<String, Object> validateToken(String token) { // JWT 토큰을 검증(validate) 하고, 그 안에 들어있는 정보를 꺼내오는 역할
        Map<String, Object> claim = null;
        try {
            SecretKey key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
            claim = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody(); // 검증이 성공하면, .getBody()로 토큰의 payload에 들어있는 클레임 정보(Map 형태) 를 꺼내옵니다.
        } catch (MalformedJwtException malformedJwtException) {
            throw new CustomJWTException("MalFormed");
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomJWTException("Expired");
        } catch (InvalidClaimException invalidClaimException) {
            throw new CustomJWTException("Invalid");
        } catch (JwtException jwtException) {
            throw new CustomJWTException("JWTError");
        } catch (Exception e) {
            throw new CustomJWTException("Error");
        }
        return claim;
    }
}
