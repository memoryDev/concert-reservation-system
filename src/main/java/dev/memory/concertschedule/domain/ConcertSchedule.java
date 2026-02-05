package dev.memory.concertschedule.domain;

import dev.memory.common.enums.DelStatus;
import dev.memory.concert.domain.Concert;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

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
    @Comment("콘서트 고유 ID")
    private Concert concert;

    @Comment("총 좌석수")
    private Integer totalSeats;

    @Comment("회당 최소 선택 단위")
    private Integer minPurchaseCount;

    @Comment("회당 최대 선택 단위")
    private Integer maxPurchaseCount;

    @Comment("공연 날짜(yyyy-MM-dd)")
    private LocalDate concertDate;

    @Comment("공연 시작 시간(HH:mm)")
    private LocalTime startTime;

    @Comment("공연 종료 시간(HH:mm)")
    private LocalTime endTime;

    @Comment("예매 시작일")
    private LocalDateTime scheduleStart;

    @Comment("예매 종료일")
    private LocalDateTime scheduleEnd;

    @Enumerated(EnumType.STRING)
    @Comment("예매 상태(PENDING: 대기, ACTIVE: 활성, CLOSED: 종료)")
    private BookingStatus bookingStatus;

    @Comment("생성일")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Comment("삭제여부(Y/N)")
    private DelStatus delStatus;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        delStatus = DelStatus.N;
        if (bookingStatus == null) {
            bookingStatus = BookingStatus.PENDING;
        }
    }

    public static ConcertSchedule createConcertSchedule(Concert concert, Integer totalSeats,
                                                        Integer minPurchaseCount, Integer maxPurchaseCount,
                                                        LocalDate concertDate, LocalTime startTime,
                                                        LocalTime endTime, LocalDateTime scheduleStart,
                                                        LocalDateTime scheduleEnd) {
        ConcertSchedule concertSchedule = new ConcertSchedule();
        concertSchedule.concert = concert;
        concertSchedule.totalSeats = totalSeats;
        concertSchedule.minPurchaseCount = minPurchaseCount;
        concertSchedule.maxPurchaseCount = maxPurchaseCount;
        concertSchedule.concertDate = concertDate;
        concertSchedule.startTime = startTime.truncatedTo(ChronoUnit.MINUTES);
        concertSchedule.endTime = endTime.truncatedTo(ChronoUnit.MINUTES);
        concertSchedule.scheduleStart = scheduleStart.truncatedTo(ChronoUnit.MINUTES);
        concertSchedule.scheduleEnd = scheduleEnd.truncatedTo(ChronoUnit.MINUTES);
        concertSchedule.bookingStatus = BookingStatus.PENDING;

        return concertSchedule;
    }

    // 예매 활성화
    public void activateBooking() {
        this.bookingStatus = BookingStatus.ACTIVE;
    }

    // 예매 종료
    public void closeBooking() {
        this.bookingStatus = BookingStatus.CLOSED;
    }

    // 예매 가능 여부 확인
    public boolean isBookingAvailable() {
        return this.bookingStatus == BookingStatus.ACTIVE;
    }
}