package dev.memory.concert.service;

import dev.memory.concert.domain.Concert;
import dev.memory.concert.dto.ConcertCreateRequest;
import dev.memory.concert.dto.ConcertResponse;
import dev.memory.concert.dto.ConcertScheduleCreateRequest;
import dev.memory.concert.repository.ConcertRepository;
import dev.memory.concertschedule.domain.ConcertSchedule;
import dev.memory.concertschedule.repository.ConcertScheduleRepository;
import dev.memory.seat.domain.Seat;
import dev.memory.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertScheduleRepository concertScheduleRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public void createConcertByAdmin(Long id, ConcertCreateRequest request) {

        // 공연정보 저장
        Concert concert = Concert.createConcert(request.getName(), request.getPrice());
        concertRepository.save(concert);

        //공연 스케쥴 저장
        List<ConcertScheduleCreateRequest> schedules = request.getSchedules();
        List<ConcertSchedule> concertSchedules = new ArrayList<>();

        for (ConcertScheduleCreateRequest schedule : schedules) {
            ConcertSchedule concertSchedule = ConcertSchedule.createConcertSchedule(
                    concert,
                    schedule.getTotalSeats(),
                    schedule.getMinPurchaseCount(),
                    schedule.getMaxPurchaseCount(),
                    schedule.getConcertDate(),
                    schedule.getStartTime(),
                    schedule.getEndTime(),
                    schedule.getScheduleStart(),
                    schedule.getScheduleEnd()
            );

            concertSchedules.add(concertSchedule);
        }
        concertScheduleRepository.saveAll(concertSchedules);


        // 시트 저장
        for (ConcertSchedule concertSchedule : concertSchedules) {
            int seatNum = 1;
            Integer totalSeats = concertSchedule.getTotalSeats();
            List<Seat> seats = new ArrayList<>();

            for (int i = 0; i < totalSeats; i++) {
                seats.add(Seat.createSeat(concertSchedule, "A" + seatNum++));
            }

            seatRepository.saveAll(seats);
        }
    }

    public Page<ConcertResponse> getConcerts(Pageable pageable) {
        // 1. 콘서트 목록 조회
        return concertRepository.getContents(pageable);
    }
}
