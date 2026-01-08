package dev.memory.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DelStatus {
    Y("삭제됨"),
    N("사용 중");

    private final String description;
}
