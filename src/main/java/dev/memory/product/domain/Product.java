package dev.memory.product.domain;

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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("상품 고유 ID")
    private Long id;

    @Comment("상품 이름")
    private String name;

    @Comment("상품 가격")
    private Long price;

    @Comment("상품 재고수")
    private Long stock;

    @Comment("판매 시작 시간")
    private LocalDateTime saleStartTime;

    @Comment("판매 종료 시간")
    private LocalDateTime saleEndTime;

    @Comment("상품 생성일")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }


}
