package dev.memory.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MeResponse {
    private Long id;
    private String userId;
    private String name;
    private String nickname;

    public static MeResponse from(Long id, String userId, String name, String nickname) {
        return new MeResponse(id, userId, name, nickname);
    }


}
