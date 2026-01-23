package dev.memory.coupon.domain;

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
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("쿠폰 고유 ID")
    private Long id;

    @Comment("쿠폰 CODE(yyyyMMdd-UUID(8자리)")
    private String code;

    @Comment("쿠폰 이름")
    private String name;

    @Comment("총 발행 개수")
    private Integer totalQuantity = 0;

    @Comment("현재 발급된 개수")
    private Integer issuedQuantity = 0;

    @Comment("발급 시작기간")
    private LocalDateTime issueStartDate;

    @Comment("발급 종료기간")
    private LocalDateTime issueEndDate;

    @Comment("할인금액(원단위)")
    private Integer discountAmount;

    @Comment("유효기간(일단위)")
    private Integer validDays;

    @Comment("활성여부(true/false)")
    private Boolean isActive = true;

    @Comment("생성일")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
