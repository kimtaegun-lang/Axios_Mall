package org.zerock.mallapi.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
@Override
public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException
//  권한이 없는 사용자가 보호된 리소스에 접근하려고 할 때 동작하는 역할을 합니다. 
{
Gson gson = new Gson();

String jsonStr = gson.toJson(Map.of("error", "ERROR_ACCESSDENIED")); 
response.setContentType("application/json"); // 응답의 콘텐츠 타입을 JSON으로 지정해서 클라이언트가 JSON임을 알게 함.
response.setStatus(HttpStatus.FORBIDDEN.value()); // HTTP 상태 코드를 403 Forbidden으로 설정.
PrintWriter printWriter = response.getWriter(); 
printWriter.println(jsonStr); 
printWriter.close();
    
}
} 