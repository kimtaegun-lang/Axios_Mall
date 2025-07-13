package org.zerock.mallapi.config;

import java.util.Arrays;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.MemberRole;
import org.zerock.mallapi.repository.Test;
import org.zerock.mallapi.security.filter.JWTCheckFilter;
import org.zerock.mallapi.security.handler.APILoginFailHandler;
import org.zerock.mallapi.security.handler.APILoginSuccessHandler;
import org.zerock.mallapi.security.handler.CustomAccessDeniedHandler;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {
    /*
     * @Configuration 설정 클래스 자동 실행됨 애플리케이션 시작 시 수동 등록 (@Bean 사용)
     * 
     * @Bean 객체를 수동으로 등록 자동 실행됨 @Configuration 클래스 로딩 시 수동 등록 (직접 메서드로 생성)
     * 
     * @Service 비즈니스 로직 클래스 자동 실행됨 컴포넌트 스캔 시 자동 등록 자동 등록 (@Component 계열)
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Spring Security의 HTTP 보안 설정을 커스터마이징
        log.info("	security config	");
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
            // 프론트엔드(예: React)가 다른 도메인에서 API 요청해도 CORS 허용되도록 처리.
            // cors란? 다른 출처(origin)의 웹페이지에서 현재 서버로 요청을 보낼 때, 서버가 그걸 허용할지를 정하는 정책
            // 사용안하는 이유는 세션 토큰 방식이 아닌 jwt 토큰 방식이므로
            // jwt: 클라이언트가 로그인으로 요청을 보내면 서버는 토큰을 발행해 클라이언트의 로컬 저장소에저장,
            // 그 후 클라이언트가 로그인 요청을 한다면? 서버는 그 토큰이 유효한지만 확인하여 로그인을 시킴
        });

        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // 서버가 클라이언트 상태를 전혀 저장하지 않겠다는 의미입니다. 즉, 세션 쿠키를 만들지 않고, 매 요청마다 인증 정보를 따로 처리함
        http.csrf(config -> config.disable());
        // CSRF(Cross-Site Request Forgery) 방어 기능을 끄는 설정입니다.
        // scrf란? 사용자가 로그인한 상태에서 공격자가 악성 스크립트를 통해 요청을 보내는 공격 방식입니다.

        http.formLogin(config -> {
            config.loginPage("/api/member/login"); // 스프링 시큐리티가 기본으로 제공하는 로그인 화면을 사용자 정의 로그인 페이지로 바꾸는 설정
  
            //config.loginProcessingUrl("/api/member/login");
           config.successHandler(new APILoginSuccessHandler()); // 후처리 부분
           config.failureHandler(new APILoginFailHandler());
        });

        http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class); // 스프링 시큐리티 필터 체인에 JWTCheckFilter 라는 커스텀 필터를  //UsernamePasswordAuthenticationFilter 필터 바로 앞에 추가한다는 뜻입니다.
                                                                        // usernamepasswordAuthenticationFilter은 로그인 폼 인증기반
        http.exceptionHandling(config-> 
        {config.accessDeniedHandler(new CustomAccessDeniedHandler());});                                                               
                                                                                
         return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // Arrays.asList는 array를 리스트로 변환
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // 즉, http://example.com, http://localhost:3000, https://mydomain.com 등 모든 도메인에서
        // 오는 요청을 허용한다는 의미
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE")); // 허용할 HTTP 메서드 종류 명시.
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        // 요청 헤더 중에서 서버가 허용할 헤더 이름 목록. 예를 들어, 인증 토큰인 Authorization 헤더를 꼭 허용해야 로그인/인증 처리
        // 가능.
        configuration.setAllowCredentials(true); // 자격 증명(쿠키, 인증 헤더 등)을 함께 보낼 수 있게 허용한다는 뜻.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // URL 패턴별로
                                                                                        // CORS정책(CorsConfiguration)을
                                                                                        // 관리하는저장소
        source.registerCorsConfiguration("/**", configuration); // 모든 API 경로(/**)에 위 CORS 정책을 적용하겠다는 의미. (api엔드 포인트란?
                                                                // controller에서 url로 매핑하는걸 말한다.)

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder(); // 비밀번호 암호화

    }

}
