package dev.memory.queue.service;

import dev.memory.common.exception.CustomException;
import dev.memory.common.exception.ErrorCode;
import dev.memory.queue.domain.QueueEntryResponse;
import dev.memory.queue.repository.QueueManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {

    private final QueueManager queueManager;


    public QueueEntryResponse queue(Long scheduleId, Long memberId) {

        // 1. 콘서트 예매 가능한지 체크
        boolean scheduleActive = queueManager.isScheduleActive(scheduleId);
        if (!scheduleActive) {
            log.warn("Schedule {} is not active", scheduleId);
            throw new CustomException(ErrorCode.SCHEDULE_NOT_ACTIVE);
        }

        // 2. 이미 활성 사용자인지 체크 (추가)
        if (queueManager.isActiveUser(scheduleId, memberId)) {
            log.info("Member {} is already active for schedule {}", memberId, scheduleId);
            return new QueueEntryResponse(0L);  // 활성 사용자는 순번 0
        }

        // 3. 대기열 진입 (반환값 체크 추가)
        boolean added = queueManager.addToWaitQueue(scheduleId, memberId);

        if (!added) {
            log.debug("Member {} was not added to queue (already exists)", memberId);
        }

        // 4. 대기 순번 확인 (null 체크 추가)
        Long rank = queueManager.getRank(scheduleId, memberId);

        if (rank == null) {
            log.error("Failed to get rank for member {} in schedule {}", memberId, scheduleId);
            throw new CustomException(ErrorCode.QUEUE_ENTRY_FAILED);
        }

        log.info("Member {} entered queue for schedule {} at rank {}", memberId, scheduleId, rank);
        return new QueueEntryResponse(rank);
    }

    /**
     * 현재 대기 순번 조회
     */
    public QueueEntryResponse getQueueStatus(Long scheduleId, Long memberId) {

        // 활성 사용자인지 먼저 체크
        if (queueManager.isActiveUser(scheduleId, memberId)) {
            return new QueueEntryResponse(0L);
        }

        // 대기 순번 확인
        Long rank = queueManager.getRank(scheduleId, memberId);

        if (rank == null) {
            log.warn("Member {} not found in queue for schedule {}", memberId, scheduleId);
            throw new CustomException(ErrorCode.NOT_IN_QUEUE);
        }

        return new QueueEntryResponse(rank);
    }

    /**
     * 예매 완료 후 활성 사용자에서 제거
     */
    public void completeReservation(Long scheduleId, Long memberId) {
        queueManager.removeActiveUser(scheduleId, memberId);
        log.info("Member {} completed reservation for schedule {}", memberId, scheduleId);
    }

}
