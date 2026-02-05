package dev.memory.concertschedule.repository;

import dev.memory.concert.dto.ConcertResponse;
import dev.memory.concert.dto.ConcertScheduleResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertScheduleCustom {
    List<ConcertScheduleResponse> findTargetConcerts();
    List<ConcertScheduleResponse> findSchedulesToEnd();
}
