package dev.memory.concert.controller;

import dev.memory.concert.dto.ConcertCreateRequest;
import dev.memory.concert.service.ConcertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/concerts")
public class ConcertRestController {

    private final ConcertService concertService;

    @PostMapping()
    public ResponseEntity<Void> createConcertByAdmin(
            @AuthenticationPrincipal User user,
            @RequestBody ConcertCreateRequest request) {

        // TODO 유효성 검증 했다는 가정하에
        Long id = Long.parseLong(user.getUsername());

        System.out.println("===== request =====");
        System.out.println(request);

        concertService.createConcertByAdmin(id, request);

        return ResponseEntity.ok().build();
    }
}
