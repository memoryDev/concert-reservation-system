package dev.memory.member.domain;

import dev.memory.common.enums.DelStatus;
import dev.memory.common.enums.MemberRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("고유 ID")
    private Long id;

    @Column(unique = true)
    @Comment("로그인 아이디")
    private String userId;

    @Column(nullable = false)
    @Comment("로그인 비밀번호")
    private String password;
    
    @Column(nullable = false)
    @Comment("닉네임")
    private String nickname;

    @Comment("이름")
    private String name;

    @Comment("생성일")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Comment("탈퇴여부(Y/N)")
    private DelStatus delStatus;

    @Enumerated(EnumType.STRING)
    @Comment("로그인권한(ROLE_USER, ROLE_ADMIN)")
    private MemberRole role;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        delStatus = DelStatus.N;
    }

    public static Member createMember(String userId, String password, String nickname, String name, MemberRole role) {
        Member member = new Member();
        member.userId = userId;
        member.password = password;
        member.nickname = nickname;
        member.name = name;
        member.role = role;
        return member;
    }


}
