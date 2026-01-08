package dev.memory.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public enum MemberRole {

    ROLE_USER("user", "일반유저"),
    ROLE_ADMIN("admin", "관리자");

    private String role;
    private String description;
}
