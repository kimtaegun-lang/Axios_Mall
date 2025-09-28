package org.zerock.mallapi.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.mallapi.util.CustomJWTException;
import org.zerock.mallapi.util.JWTUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APIRefreshController { // 클라이언트가 가지고 있던 Access Token이 만료됐을 경우 Refresh Token을 통해 새로운 Access Token을 발급해주는 API.

    @RequestMapping("/api/member/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader,@RequestParam("refreshToken") String refreshToken) {

        if (refreshToken == null) { // refreshToken이 널일경우,
            throw new CustomJWTException("NULL_REFRASH");
        }

        if (authHeader == null || authHeader.length() < 7) { // 헤더가 비정상 적일경우
            throw new CustomJWTException("INVALID_STRING");
        }

        String accessToken = authHeader.substring(7); // access 토큰 가져오기

        // Access 토큰이 만료되지 않았다면
        if (!checkExpiredToken(accessToken)) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        // Refresh 토큰 검증
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
        log.info("refresh ... claims: " + claims);

        String newAccessToken = JWTUtil.generateToken(claims, 10); // 기존 토큰의 claims(이메일, 권한 등) 정보를 재사용해서 유효기간 10분짜리 Access Token을 새로 생성해.

        String newRefreshToken = checkTime((Integer) claims.get("exp")) 
                ? JWTUtil.generateToken(claims, 60 * 24) // 24시간짜리 발행
                : refreshToken; // 기존꺼 재사용

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }

    // 시간이 1시간 미만으로 남았다면 true 반환
    private boolean checkTime(Integer exp) { // 기존 refresh token의 유효시간이 1시간도 안남앗으면,
        // JWT exp를 날짜로 변환
        java.util.Date expDate = new java.util.Date((long) exp * 1000); 
        // 현재 시간과의 차이 계산 (밀리초)
        long gap = expDate.getTime() - System.currentTimeMillis();
        // 분 단위로 환산
        long leftMin = gap / (1000 * 60);
        return leftMin < 60;
    }

    private boolean checkExpiredToken(String token) { // Access Token이 이미 만료되었는지 검사하는 용도.
        try {
            JWTUtil.validateToken(token);
        } catch (CustomJWTException ex) {
            if ("Expired".equals(ex.getMessage())) {
                return true;
            }
        }
        return false;
    }
}
