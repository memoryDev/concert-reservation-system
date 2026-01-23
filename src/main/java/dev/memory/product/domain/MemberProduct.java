package dev.memory.product.domain;

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
public class MemberProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("주문(구매) 고유 ID")
    private Long id;

    @Comment("구매한 회원")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Comment("구매한 상품")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Comment("구매 당시 가격")
    @Column(nullable = false)
    private Long orderPrice;

    @Comment("구매 수량")
    @Column(nullable = false)
    private Long count;

    @Comment("취소 여부(true/false)")
    @Column(nullable = false)
    private Boolean status;

    @Comment("주문 일시")
    private LocalDateTime orderedAt;

    @PrePersist
    public void prePersist() {
        this.orderedAt = LocalDateTime.now();
        this.status = false;
    }
}
