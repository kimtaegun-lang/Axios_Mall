package org.zerock.mallapi.config;
import org.springframework.context.annotation.Configuration; 
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; 
import org.zerock.mallapi.controller.formatter.LocalDateFormatter;

@Configuration
public class CustomServletConfig implements WebMvcConfigurer{

    @Override
    public void addFormatters(FormatterRegistry registry)
    {
        registry.addFormatter(new LocalDateFormatter()); // "2025-07-09" 같은 문자열을 자동으로 LocalDate로 변환해줍니다.
    }

    /* 
    @Override // 외부 도메인에서 백엔드에 접근할 수 있게 허용함
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**").allowedOrigins("*") // 모든 경로에 대해 허용 // 어떤 출처(origin)에서든 접근 가능 (보안상 위험할 수 있음)
        .allowedMethods("HEAD","GET","POST","PUT","DELETE","OPTIONS") // 	허용할 HTTP 메서드 지정
        .maxAge(300) //브라우저가 preflight 요청 결과를 300초 동안 캐싱
        .allowedHeaders("Authorization","Cache-Control","Content-Type"); // 요청에 포함할 수 있는 헤더 지정	
    }
    */
}
