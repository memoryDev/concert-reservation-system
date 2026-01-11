package dev.memory.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        // 1. 상태 코드 설정 (401)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 2. 응답 타입 설정 (JSON)
        response.setContentType("application/json;charset=UTF-8");

        // 3. 응답 바디 만들기 (우리가 만든 ErrorResponse 재활용!)
        // 원래는 구체적인 에러(만료 vs 위조)를 구분하려면 request attribute를 확인해야 하지만,
        // 일단은 통상적인 "인증 실패" 메시지로 처리합니다.
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(401)
                .error("UNAUTHORIZED")
                .message("인증이 필요하거나 토큰이 만료되었습니다.") // 또는 ErrorCode.UNAUTHORIZED.getMessage()
                .build();

        // 4. JSON으로 변환해서 응답에 쓰기
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

    }
}
