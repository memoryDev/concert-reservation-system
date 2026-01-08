package dev.memory.concertschedule.domain;

import dev.memory.common.enums.DelStatus;
import dev.memory.concert.domain.Concert;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Comment("콘서트 일정 테이블")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConcertSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("일정 고유 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Comment("총 좌석수")
    private Integer totalSeats;

    @Comment("회당 최소 선택 단위")
    private Integer minPurchaseCount;

    @Comment("회당 최대 선택 단위")
    private Integer maxPurchaseCount;

    @Comment("예매 시작일")
    private LocalDateTime scheduleStart;

    @Comment("예매 종료일")
    private LocalDateTime scheduleEnd;

    @Comment("생성일")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Comment("삭제여부(Y/N)")
    private DelStatus delStatus;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        delStatus = DelStatus.N;
    }

}
