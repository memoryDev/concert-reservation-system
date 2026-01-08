package dev.memory.concertreservation.domain;

import dev.memory.common.enums.DelStatus;
import dev.memory.member.domain.Member;
import dev.memory.seat.domain.Seat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Comment("콘서트 예약 정보 테이블")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConcertReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("고유 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Comment("유저 고유 ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    @Comment("좌석 고유 ID")
    private Seat seat;

    @Comment("생성일")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Comment("에약상태(COMPLETE, CANCEL)")
    private ConStatus conStatus;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

}
