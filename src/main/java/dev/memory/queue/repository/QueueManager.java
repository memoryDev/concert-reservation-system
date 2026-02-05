package dev.memory.queue.repository; // 패키지 위치 확인

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueueManager {

    private final RedisTemplate<String, String> redisTemplate;

    // Key 상수 정의
    private static final String ACTIVE_SCHEDULES_KEY = "active:schedules"; // 활성 스케줄 목록 (Set)
    private static final String WAIT_QUEUE_PREFIX = "queue:wait:";         // 대기열 (Sorted Set)
    private static final String ACTIVE_USER_PREFIX = "queue:active:";
    private static final long ACTIVE_USER_TTL_SECONDS = 600L;

    // ==========================================
    // 1. 스케줄 활성화 관리 (기존 코드 + isScheduleActive 추가)
    // ==========================================

    public void startBooking(Long scheduleId) {
        redisTemplate.opsForSet().add(ACTIVE_SCHEDULES_KEY, scheduleId.toString());
        log.info("Schedule {} booking started", scheduleId);
    }

    public void endBooking(Long scheduleId) {
        redisTemplate.opsForSet().remove(ACTIVE_SCHEDULES_KEY, scheduleId.toString());
        // 관련 대기열 및 활성 사용자 삭제
        redisTemplate.delete(getWaitKey(scheduleId));
        redisTemplate.delete(getActiveKey(scheduleId));
        log.info("Schedule {} booking ended", scheduleId);
    }

    // ★ 추가: 서비스에서 검증용으로 쓸 메서드
    public boolean isScheduleActive(Long scheduleId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(ACTIVE_SCHEDULES_KEY, scheduleId.toString()));
    }

    // ==========================================
    // 2. 대기열 관리 (ZSET 로직 추가)
    // ==========================================

    private String getWaitKey(Long scheduleId) {
        return WAIT_QUEUE_PREFIX + scheduleId;
    }

    // 대기열 등록 (ZADD)
    public boolean addToWaitQueue(Long scheduleId, Long memberId) {
        String memberIdStr = memberId.toString();

        // 이미 대기열에 있는지 체크
        if (isInWaitQueue(scheduleId, memberId)) {
            log.debug("Member {} already in wait queue for schedule {}", memberId, scheduleId);
            return false;
        }

        // 이미 활성 사용자인지 체크
        if (isActiveUser(scheduleId, memberId)) {
            log.debug("Member {} is already active for schedule {}", memberId, scheduleId);
            return false;
        }

        long now = System.currentTimeMillis();
        Boolean added = redisTemplate.opsForZSet().add(getWaitKey(scheduleId), memberIdStr, (double) now);

        if (Boolean.TRUE.equals(added)) {
            log.info("Member {} added to wait queue for schedule {}", memberId, scheduleId);
            return true;
        }
        return false;
    }

    public Set<String> getActiveSchedules() {
        return redisTemplate.opsForSet().members(ACTIVE_SCHEDULES_KEY);
    }

    // 대기열에 있는지 확인
    public boolean isInWaitQueue(Long scheduleId, Long memberId) {
        Long rank = redisTemplate.opsForZSet().rank(getWaitKey(scheduleId), memberId.toString());
        return rank != null;
    }

    // 대기열 크기 조회
    public Long getWaitQueueSize(Long scheduleId) {
        Long size = redisTemplate.opsForZSet().size(getWaitKey(scheduleId));
        return size != null ? size : 0L;
    }

    public Long getRank(Long scheduleId, Long memberId) {
        Long rank = redisTemplate.opsForZSet().rank(getWaitKey(scheduleId), memberId.toString());
        return (rank == null) ? null : rank + 1;
    }

    // popMin() 이름 변경 및 로직 추가
    public Set<String> activateUsers(Long scheduleId, int count) {
        Set<ZSetOperations.TypedTuple<String>> tuples =
                redisTemplate.opsForZSet().popMin(getWaitKey(scheduleId), count);

        if (tuples == null || tuples.isEmpty()) {
            return Collections.emptySet();
        }

        Set<String> memberIds = tuples.stream()
                .map(ZSetOperations.TypedTuple::getValue)
                .collect(Collectors.toSet());

        // ⭐ 활성 사용자로 등록 (중요!)
        for (String memberId : memberIds) {
            addToActiveUsers(scheduleId, memberId);
        }

        log.info("Activated {} users for schedule {}", memberIds.size(), scheduleId);
        return memberIds;
    }

    // ==========================================
// 3. 활성 사용자 관리 (SET with TTL)
// ==========================================

    private String getActiveKey(Long scheduleId) {
        return ACTIVE_USER_PREFIX + scheduleId;
    }

    // 활성 사용자 추가 (TTL 설정)
    private void addToActiveUsers(Long scheduleId, String memberId) {
        String key = getActiveKey(scheduleId);
        redisTemplate.opsForSet().add(key, memberId);
        // TTL 설정 (10분)
        redisTemplate.expire(key, ACTIVE_USER_TTL_SECONDS, TimeUnit.SECONDS);
        log.debug("Member {} activated for schedule {} with TTL {}s", memberId, scheduleId, ACTIVE_USER_TTL_SECONDS);
    }

    // 활성 사용자 확인
    public boolean isActiveUser(Long scheduleId, Long memberId) {
        return Boolean.TRUE.equals(
                redisTemplate.opsForSet().isMember(getActiveKey(scheduleId), memberId.toString())
        );
    }

    // 활성 사용자 제거 (예매 완료 시)
    public void removeActiveUser(Long scheduleId, Long memberId) {
        redisTemplate.opsForSet().remove(getActiveKey(scheduleId), memberId.toString());
        log.info("Member {} removed from active users for schedule {}", memberId, scheduleId);
    }

    // 활성 사용자 수 조회
    public Long getActiveUserCount(Long scheduleId) {
        Long size = redisTemplate.opsForSet().size(getActiveKey(scheduleId));
        return size != null ? size : 0L;
    }

    // 활성 사용자 목록 조회
    public Set<String> getActiveUsers(Long scheduleId) {
        Set<String> members = redisTemplate.opsForSet().members(getActiveKey(scheduleId));
        return members != null ? members : Collections.emptySet();
    }
}