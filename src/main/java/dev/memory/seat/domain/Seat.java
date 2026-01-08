package dev.memory.seat.domain;

import dev.memory.common.enums.DelStatus;
import dev.memory.concertschedule.domain.ConcertSchedule;
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
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("좌석 고유 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_schedule_id")
    private ConcertSchedule concertSchedule;

    @Column(nullable = false)
    @Comment("좌석 이름")
    private String seatLabel;

    @Comment("생성일")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Comment("탈퇴여부(Y/N)")
    private DelStatus delStatus;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        delStatus = DelStatus.N;
    }
}
