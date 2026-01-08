package dev.memory.common.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.token-validity-in-seconds}")
    private Long tokenValidityInSeconds;

    public void createToken(Authentication authentication) {

        // 사용자의 권한 목록을 문자열로 합치기
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Object principal = authentication.getPrincipal();
        // TODO CustomUserDetails 만들어야함
        if (principal instanceof Object) {
            // TODO principal 이게 기본 Object인데 CustomUserDetils 로 변환해야함

        }


    }



}
