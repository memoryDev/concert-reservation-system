package dev.memory.concertreservation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public enum ConStatus {

    COMPLETE("예약완료"),
    CANCEL("예약취소");

    private String description;
}
