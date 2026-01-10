package dev.memory.auth.controller;

import dev.memory.auth.dto.LoginRequest;
import dev.memory.auth.dto.LoginResponse;
import dev.memory.auth.dto.MeResponse;
import dev.memory.auth.service.AuthService;
import dev.memory.common.jwt.TokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        System.out.println(request);

        // TODO 유효성 검사(체크했다고 침)
        TokenInfo tokenInfo = authService.login(request);

        return ResponseEntity.ok()
                .body(LoginResponse.builder().accessToken(tokenInfo.getAccessToken()).build());
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(
            @AuthenticationPrincipal User user
            ) {

        Long id = Long.parseLong(user.getUsername());
        MeResponse meResponse = authService.me(id);

        return ResponseEntity.ok().body(meResponse);

    }
}
