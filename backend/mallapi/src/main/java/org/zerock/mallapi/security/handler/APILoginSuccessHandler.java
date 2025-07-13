package org.zerock.mallapi.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.mallapi.dto.MemberDTO;
import org.zerock.mallapi.util.JWTUtil;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        log.info("--------------------------------------");
        log.info(authentication);
        log.info("--------------------------------------");
        MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();
        /*
         * 로그인 성공 시 Spring Security가 생성한 인증 객체(Authentication)에서 로그인한 사용자 정보를 꺼냅니다.
         * getPrincipal()은 UserDetails
         */
        /*
        전체 흐름: 
         * 사용자가 POST /api/member/login 으로 로그인 요청을 보냅니다.
         * 
         * 스프링 시큐리티가 내부적으로 UsernamePasswordAuthenticationFilter를 실행합니다.
         * 
         * 그 필터는 내부적으로 UserDetailsService의 loadUserByUsername()을 호출합니다.
         * 
         * 거기서 리턴한 UserDetails 객체 — 즉, 당신이 만든 MemberDTO — 가 Authentication의 principal로
         * 들어갑니다.
         * 
         */
        Map<String, Object> claims = memberDTO.getClaims();

        String accessToken = JWTUtil.generateToken(claims, 10); // 만료시간 10분짜리 액세스 토큰(access token)**을 생성 
        String refreshToken = JWTUtil.generateToken(claims,60*24); // 만료시간 24시간(60분 × 24)짜리 리프레시 토큰(refresh token)**을 생성
        claims.put("accessToken", accessToken);
        claims.put("refreshToken", refreshToken);
        // JWT 토큰을 만들기 위해 claim 정보를 넣는 곳
        // jwt 토큰 만료 시간이 아닌, 인증을 위한 시간이라고 보면됨ㄴ

        Gson gson = new Gson();
        String jsonStr = gson.toJson(claims);
        // claims 맵을 JSON 문자열로 변환
        response.setContentType("application/json");

        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonStr); // 서버가 만든 JWT 토큰이 포함된 JSON 문자열을 HTTP 응답 본문(response body) 으로 클라이언트에 보내주는 곳입니다.
        printWriter.close();
        /*
         * 클라이언트에게 Content-Type: application/json 으로 응답하게 하고,
         * jsonStr을 HTTP 응답 본문으로 보냄
         */
    }

}
