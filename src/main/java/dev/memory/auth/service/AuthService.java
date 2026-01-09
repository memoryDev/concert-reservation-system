package dev.memory.auth.service;

import dev.memory.auth.dto.LoginRequest;
import dev.memory.auth.dto.MeResponse;
import dev.memory.common.enums.DelStatus;
import dev.memory.common.jwt.JwtProvider;
import dev.memory.common.jwt.TokenInfo;
import dev.memory.member.domain.Member;
import dev.memory.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final MemberRepository memberRepository;

    public TokenInfo login(LoginRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword());

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(token);

        return jwtProvider.createToken(authenticate);
    }

    public MeResponse me(Long id) {

        // 1. 회원 정보 조회
        Member member = memberRepository.findByIdAndDelStatus(id, DelStatus.N)
                .orElseThrow(() ->
                        new UsernameNotFoundException("로그인 정보가 존재하지 않습니다."));

        return MeResponse.from(member.getId(), member.getUserId(), member.getName(), member.getNickname());
    }

}
