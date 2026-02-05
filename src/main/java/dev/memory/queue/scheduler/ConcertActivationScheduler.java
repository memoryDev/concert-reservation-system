package dev.memory.queue.scheduler;

import dev.memory.concert.dto.ConcertScheduleResponse;
import dev.memory.concert.repository.ConcertRepository;
import dev.memory.concertschedule.repository.ConcertScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConcertActivationScheduler {

    private final ConcertRepository concertRepository;
    private final ConcertScheduleRepository concertScheduleRepository;
    private final dev.memory.queue.repository.QueueManager queueManager;

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void activateBooking() {
        // 1. 현재 시간이 예매 시작 시간에 도달한 공연 중
        // 아직 활성화 되지 않은 공연 리스트 조회
        List<ConcertScheduleResponse> targetConcerts = concertScheduleRepository.findTargetConcerts();

        if (targetConcerts.isEmpty()) {
            return;
        }

        for (ConcertScheduleResponse schedule : targetConcerts) {
            // Redis에 스케줄 ID 등록
            queueManager.startBooking(schedule.getId());

            // 예매 상태 변경
            concertScheduleRepository.findById(schedule.getId()).ifPresent((entity) -> {
                entity.activateBooking();
            });

        }

    }

    /**
     * 10초마다 예매 종료할 스케줄 처리
     */
    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void endBooking() {
        try {
            // 1. 예매 종료 시간이 지난 활성 스케줄 조회
            List<ConcertScheduleResponse> schedulesToEnd = concertScheduleRepository.findSchedulesToEnd();

            if (schedulesToEnd.isEmpty()) {
                return;
            }

            log.info("Found {} schedules to end", schedulesToEnd.size());

            for (ConcertScheduleResponse schedule : schedulesToEnd) {
                try {
                    // Redis에서 스케줄 제거 (대기열, 활성 사용자 모두 삭제)
                    queueManager.endBooking(schedule.getId());

                    // 예매 상태 변경
                    concertScheduleRepository.findById(schedule.getId()).ifPresent(entity -> {
                        entity.closeBooking();
                        log.info("Schedule {} booking closed", schedule.getId());
                    });
                } catch (Exception e) {
                    log.error("Failed to end schedule {}", schedule.getId(), e);
                }
            }
        } catch (Exception e) {
            log.error("Error in endBooking", e);
        }
    }
}
