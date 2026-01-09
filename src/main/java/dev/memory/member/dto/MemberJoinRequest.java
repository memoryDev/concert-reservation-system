package dev.memory.member.dto;

import dev.memory.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberJoinRequest {
    private String userId;
    private String password;
    private String name;
    private String nickname;
}
