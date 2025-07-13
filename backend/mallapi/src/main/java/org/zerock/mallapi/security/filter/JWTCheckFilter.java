package org.zerock.mallapi.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.mallapi.dto.MemberDTO;
import org.zerock.mallapi.util.JWTUtil;

import com.google.gson.Gson;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        // 이 메서드는 필터를 건너뛸지 말지를 결정합니다.
        // return false; 일 경우 모든 요청에 대해 필터를 적용

        // cors 사전 요청은 제외
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String path = request.getRequestURI();
        log.info("check uri.............." + path);

        // api/member/ 경로의 호출은 체크하지 않음
        if (path.startsWith("/api/member/")) {
            return true;
        }

        // 이미지 조회 경로는 체크하지 않음
        if (path.startsWith("/api/products/view/")) {
            return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("-----------------JWTCheckFilter.................");
        String authHeaderStr = request.getHeader("Authorization");
        // 클라이언트로부터 authorization 헤더를 받음
        try {

            String accessToken = authHeaderStr.substring(7); // barea.. 7글자 자르고 토큰만 받아옴
            Map<String, Object> claims = JWTUtil.validateToken(accessToken); // 유효한 토큰이면 claims 정보를 얻음
            log.info("JWT claims: " + claims);
            String email = (String) claims.get("email");
            String pw = (String) claims.get("pw");
            String nickname = (String) claims.get("nickname");
            Boolean social = (Boolean) claims.get("social");
            List<String> roleNames = (List<String>) claims.get("roleNames");

            MemberDTO memberDTO = new MemberDTO(email, pw, nickname, social.booleanValue(),
                    roleNames);
            log.info("-----------------------------------	");
            log.info(memberDTO);
            log.info(memberDTO.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDTO,
                    pw, memberDTO.getAuthorities());
                // 현재 실행 중인 쓰레드의 보안 컨텍스트(SecurityContext)에 인증 객체 등록
                // 즉, 인증 완료 상태로 설정하여 이후에 권한 체크 및 인증된 사용자로 인식하도록 함
                /*SecurityContextHolder.getContext().setAuthentication(authenticationToken); 으로 인증 컨텍스트에 등록하지 않으면

스프링 시큐리티가 현재 요청이 인증된 사용자 요청인지 인식하지 못해서

@PreAuthorize, @Secured 같은 권한 관련 어노테이션이 동작하지 않습니다!  인증 컨텍스트는 스프링 시큐리티가 알아서 만들어줌*/
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
            // 필터 체인의 다음 필터로 요청과 응답을 전달
        }
        // "Bearer"는 JWT 토큰이 HTTP 헤더에 담기는 방식 중 하나임
        catch (Exception e) {

            log.error("JWT Check Error..............");
            log.error(e.getMessage());
            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();
        }
    }
}
