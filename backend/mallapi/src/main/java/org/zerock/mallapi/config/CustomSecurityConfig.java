package org.zerock.mallapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
@RequiredArgsConstructor
public class CustomSecurityConfig {

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

log.info("	security config	");

http.cors(httpSecurityCorsConfigurer -> {
httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
});

return

    
}
