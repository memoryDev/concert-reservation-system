package dev.memory.member.controller;

import dev.memory.member.dto.MemberJoinRequest;
import dev.memory.member.dto.MemberJoinResponse;
import dev.memory.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/members")
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<MemberJoinResponse> join(@RequestBody MemberJoinRequest request) {

        // TODO 유효성 검사 체크 완료
        Long createdId = memberService.join(request);

        return ResponseEntity.ok().body(MemberJoinResponse.builder().createdId(createdId).build());
    }
}
