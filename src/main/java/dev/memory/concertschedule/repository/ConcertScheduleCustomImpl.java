package dev.memory.concertschedule.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.memory.common.enums.DelStatus;
import dev.memory.concert.domain.QConcert;
import dev.memory.concert.dto.ConcertResponse;
import dev.memory.concert.dto.ConcertScheduleResponse;
import dev.memory.concertschedule.domain.BookingStatus;
import dev.memory.concertschedule.domain.ConcertSchedule;
import dev.memory.concertschedule.domain.QConcertSchedule;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static dev.memory.concert.domain.QConcert.concert;
import static dev.memory.concertschedule.domain.QConcertSchedule.concertSchedule;

@RequiredArgsConstructor
public class ConcertScheduleCustomImpl implements ConcertScheduleCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ConcertScheduleResponse> findTargetConcerts() {
        // 현재 시간의 '초'까지는 유지하되, 나노초를 가장 큰 값으로 설정하여
        // DB의 .000000 ~ .999999 범위를 모두 커버하게 합니다.
        LocalDateTime now = LocalDateTime.now()
                .withNano(999_999_999);

        return queryFactory
                .select(Projections.constructor(ConcertScheduleResponse.class,
                        concertSchedule.id
                ))
                .from(concertSchedule)
                .where(
                        concertSchedule.scheduleStart.loe(now),
                        concertSchedule.scheduleEnd.goe(now),
                        concertSchedule.bookingStatus.eq(BookingStatus.PENDING),  // ⬅️ 추가
                        concertSchedule.delStatus.eq(DelStatus.N)
                )
                .fetch();
    }

    /**
     * 예매 종료할 스케줄 조회
     */
    @Override
    public List<ConcertScheduleResponse> findSchedulesToEnd() {
        LocalDateTime now = LocalDateTime.now()
                .withNano(999_999_999);

        return queryFactory
                .select(Projections.constructor(ConcertScheduleResponse.class,
                        concertSchedule.id
                ))
                .from(concertSchedule)
                .where(
                        concertSchedule.scheduleEnd.lt(now),
                        concertSchedule.bookingStatus.eq(BookingStatus.ACTIVE),
                        concertSchedule.delStatus.eq(DelStatus.N)
                )
                .fetch();
    }
}
