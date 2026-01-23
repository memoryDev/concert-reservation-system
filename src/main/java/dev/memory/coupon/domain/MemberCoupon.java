package dev.memory.coupon.domain;

import dev.memory.member.domain.Member;
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
public class MemberCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("유저쿠폰 고유 ID")
    private Long id;

    @Comment("발급받은 유저 고유 ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Comment("발급받은 쿠폰 고유 ID")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Comment("발급일")
    private LocalDateTime issuedAt;

    @Comment("만료일")
    private LocalDateTime expireAt;

    @Comment("사용여부(true/false)")
    private Boolean isUsed = false;

    @Comment("사용일")
    private LocalDateTime usedAt;

    @PrePersist
    public void prePersist() {
        issuedAt = LocalDateTime.now();
    }
}
