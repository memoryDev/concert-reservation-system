package dev.memory.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public enum MemberRole {

    ROLE_USER("USER", "일반유저"),
    ROLE_ADMIN("ADMIN", "관리자");

    private String role;
    private String description;
}
