package dev.memory.concert.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.memory.common.enums.DelStatus;
import dev.memory.concert.dto.ConcertResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.querydsl.core.types.Projections.*;
import static dev.memory.concert.domain.QConcert.*;
import static dev.memory.concertschedule.domain.QConcertSchedule.*;

@RequiredArgsConstructor
public class CustomConcertRepositoryImpl implements CustomConcertRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ConcertResponse> getContents(Pageable pageable) {

        // 데이터 조회
        List<ConcertResponse> content = queryFactory.select(
                        constructor(
                                ConcertResponse.class,
                                concert.id,
                                concert.name,
                                concert.price,
                                concertSchedule.concertDate.min(),
                                concertSchedule.concertDate.max()
                        )
                )
                .from(concert)
                .leftJoin(concertSchedule)
                .on(concert.id.eq(concertSchedule.concert.id)
                        .and(concertSchedule.delStatus.eq(DelStatus.N)))
                .where(concert.delStatus.eq(DelStatus.N))
                .groupBy(concert.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운터 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(concert.count())
                .from(concert)
                .where(concert.delStatus.eq(DelStatus.N));


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

}
