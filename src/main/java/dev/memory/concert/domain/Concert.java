package dev.memory.concert.domain;

import dev.memory.common.enums.DelStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "concert")
@Comment("콘서트 정보 테이블")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("콘서트 고유 ID")
    private Long id;

    @Column(nullable = false)
    @Comment("이름")
    private String name;

    @Column(nullable = false)
    @Comment("콘서트 가격")
    private Integer price;

    @Comment("생성일")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Comment("탈퇴여부(Y/N)")
    private DelStatus delStatus = DelStatus.N;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        delStatus = DelStatus.N;
    }


}
