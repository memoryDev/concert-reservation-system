package dev.memory.member.service;

import dev.memory.common.enums.DelStatus;
import dev.memory.common.enums.MemberRole;
import dev.memory.common.exception.CustomException;
import dev.memory.common.exception.ErrorCode;
import dev.memory.member.domain.Member;
import dev.memory.member.dto.MemberJoinRequest;
import dev.memory.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(MemberJoinRequest request) {

        boolean present = memberRepository.findByUserIdAndDelStatus(request.getUserId(), DelStatus.N)
                .isPresent();

        if (present) {
            throw new CustomException(ErrorCode.DUPLICATE_USER_ID);
        }

        // entity 생성
        Member member = Member.createMember(request.getUserId(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                request.getNickname(),
                MemberRole.ROLE_USER
        );

        Member createdMember = memberRepository.save(member);

        return createdMember.getId();
    }
}
