package dev.memory.queue.controller;

import dev.memory.queue.domain.QueueEntryRequest;
import dev.memory.queue.domain.QueueEntryResponse;
import dev.memory.queue.repository.QueueManager;
import dev.memory.queue.service.QueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/queue")
@Slf4j
public class QueueController {

    private final QueueService queueService;
    private final QueueManager queueManager;

    @PostMapping
    public ResponseEntity<QueueEntryResponse> queue(@RequestBody QueueEntryRequest request,
                                                    @AuthenticationPrincipal User user) {

        Long memberId = Long.parseLong(user.getUsername());
        QueueEntryResponse queue = queueService.queue(request.getScheduleId(), memberId);

        return ResponseEntity.ok().body(queue);
    }

    /**
     * 대기 순번 조회 API
     */
    @GetMapping("/status")
    public ResponseEntity<QueueEntryResponse> getQueueStatus(
            @RequestParam Long scheduleId,
            @AuthenticationPrincipal User user) {

        Long memberId = Long.parseLong(user.getUsername());
        QueueEntryResponse response = queueService.getQueueStatus(scheduleId, memberId);

        return ResponseEntity.ok(response);
    }

    /**
     * 예매 완료 후 활성 사용자에서 제거 API
     */
    @DeleteMapping("/complete")
    public ResponseEntity<Void> completeReservation(
            @RequestParam Long scheduleId,
            @AuthenticationPrincipal User user) {

        Long memberId = Long.parseLong(user.getUsername());
        queueService.completeReservation(scheduleId, memberId);

        return ResponseEntity.ok().build();
    }

}
