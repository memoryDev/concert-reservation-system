package dev.memory.concert.service;

import dev.memory.concert.domain.Concert;
import dev.memory.concert.dto.ConcertCreateRequest;
import dev.memory.concert.dto.ConcertScheduleCreateRequest;
import dev.memory.concert.repository.ConcertRepository;
import dev.memory.concertschedule.domain.ConcertSchedule;
import dev.memory.concertschedule.repository.ConcertScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertScheduleRepository concertScheduleRepository;

    @Transactional
    public void createConcertByAdmin(Long id, ConcertCreateRequest request) {

        // 공연정보 저장
        Concert concert = Concert.createConcert(request.getName(), request.getPrice());
        concertRepository.save(concert);

        //공연 스케쥴 저장
        List<ConcertScheduleCreateRequest> schedules = request.getSchedules();

        // 시트 저장




    }
}
