package dev.memory.queue.scheduler;

import dev.memory.queue.repository.QueueManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class QueueScheduler {

    private final QueueManager queueManager;

    // 한 번에 활성화할 사용자 수 (설정 파일에서 주입)
    @Value("${queue.activation.batch-size:10}")
    private int activationBatchSize;

    // 활성 사용자 최대 인원 (설정 파일에서 주입)
    @Value("${queue.activation.max-active-users:100}")
    private int maxActiveUsers;

    /**
     * 1초마다 대기열 처리
     */
    @Scheduled(fixedDelay = 1000)
    public void processQueue() {
        try {
            Set<String> activeSchedules = queueManager.getActiveSchedules();

            if (activeSchedules == null || activeSchedules.isEmpty()) {
                return;
            }

            for (String scheduleIdStr : activeSchedules) {
                try {
                    Long scheduleId = Long.parseLong(scheduleIdStr);
                    processScheduleQueue(scheduleId);
                } catch (NumberFormatException e) {
                    log.error("Invalid schedule ID format: {}", scheduleIdStr, e);
                }
            }
        } catch (Exception e) {
            log.error("Error in processQueue", e);
        }
    }

    /**
     * 특정 스케줄의 대기열 처리
     */
    private void processScheduleQueue(Long scheduleId) {
        // 현재 활성 사용자 수 확인
        Long currentActiveCount = queueManager.getActiveUserCount(scheduleId);

        if (currentActiveCount >= maxActiveUsers) {
            log.debug("Schedule {} has reached max active users ({})", scheduleId, maxActiveUsers);
            return;
        }

        // 추가로 활성화할 수 있는 인원 계산
        int availableSlots = (int) (maxActiveUsers - currentActiveCount);
        int toActivate = Math.min(availableSlots, activationBatchSize);

        // 대기열 크기 확인
        Long waitQueueSize = queueManager.getWaitQueueSize(scheduleId);
        if (waitQueueSize == 0) {
            return;
        }

        // 실제로 활성화할 인원
        toActivate = (int) Math.min(toActivate, waitQueueSize);

        // 사용자 활성화
        Set<String> activatedUsers = queueManager.activateUsers(scheduleId, toActivate);

        if (!activatedUsers.isEmpty()) {
            log.info("Schedule {}: Activated {} users. Active: {}, Waiting: {}",
                    scheduleId,
                    activatedUsers.size(),
                    currentActiveCount + activatedUsers.size(),
                    waitQueueSize - activatedUsers.size()
            );
        }
    }
}