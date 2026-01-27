package dev.memory.common.jwt;

import dev.memory.common.exception.CustomException;
import dev.memory.common.exception.ErrorCode;
import dev.memory.common.security.PrincipalDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.token-validity-in-seconds}")
    private Long tokenValidityInSeconds;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * Access Token 생성
     * 인증된 사용자 정보를 받아 JWT 토큰을 생성합니다.
     *
     * @param authentication 인증된 사용자 정보
     * @return 생성된 Access Token이 담긴 TokenInfo 객체
     */
    public TokenInfo createToken(Authentication authentication) {

        // 1. 사용자 권한 목록을 가져와서 문자열로 변환
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 2. 인증 정보에서 Principal 꺼내기
        if (!(authentication.getPrincipal() instanceof PrincipalDetails principal)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 3. 만료 시간 설정
        Date now = new Date();
        Date validity = new Date(now.getTime() + (tokenValidityInSeconds * 1000));

        // 4. JWT 토큰 생성
        String accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(String.valueOf(principal.getId()))
                .claim("auth", authorities)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, signatureAlgorithm)
                .compact();

        log.debug("id: {}, accessToken: {}", principal.getId(), accessToken);

        return TokenInfo.builder()
                .accessToken(accessToken)
                .build();
    }

    /**
     * 토큰 유효성 검증
     * 토큰을 파싱하여 위조 여부, 만료 여부 등를 확인합니다.
     *
     * @param token 검증할 JWT 토큰
     * @return 유효하지 않으면 예외 발생
     */
    public void validateToken(String token) {

        try {
            // 토큰 파싱을 시도하여 예외가 발생하지 않으면 유효한 토큰
            getClaims(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.debug("잘못된 JWT 서명입니다.");
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        } catch (ExpiredJwtException e) {
            log.debug("만료된 JWT 토큰입니다.");
            throw new CustomException(ErrorCode.SESSION_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.debug("지원되지 않는 JWT 토큰입니다.");
            throw new CustomException(ErrorCode.SESSION_EXPIRED);
        } catch (IllegalArgumentException e) {
            log.debug("JWT 토큰이 잘못되었습니다.");
            throw new CustomException(ErrorCode.EMPTY_TOKEN);
        }
    }

    /**
     * 토큰 복호화(Claims 추출
     * 토큰에서 Claims을 꺼냅니다.
     * @param token JWT 토큰
     * @return Claims 객체
     */
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 인증정보(Authentication) 조회
     * 토큰을 파싱하여 권한 정보를 꺼내고, Spring Security가 사용하는 Authentication 객체를 반환합니다.
     * DB 조회하지 않고 토큰 내부 정보만으로 객체를 생성합니다.
     * @param token JWT 토큰
     * @return UsernamePasswordAuthenticationToken (Authentication)
     */
    public Authentication getAuthentication(String token) {

        // 1. 토큰 복호화
        Claims claims = getClaims(token);

        if (claims.get("auth") == null) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        // 2. 클레임에서 권한 정보 가져오기 (문자열->리스트 변환)
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

        // 3. userDetails 객체 생성
        User principal = new User(claims.getSubject(), "", authorities);

        // 4. Authentication 객체 반환
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);

    }

}
