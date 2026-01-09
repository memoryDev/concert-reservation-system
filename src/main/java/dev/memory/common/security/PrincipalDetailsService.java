package dev.memory.common.security;

import dev.memory.common.enums.DelStatus;
import dev.memory.member.domain.Member;
import dev.memory.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. 회원 조회
        return memberRepository.findByUserIdAndDelStatus(username, DelStatus.N)
                .map(PrincipalDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("아이디 및 비밀번호가 틀립니다."));
    }
}
