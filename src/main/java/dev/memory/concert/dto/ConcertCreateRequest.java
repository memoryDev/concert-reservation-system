package dev.memory.concert.dto;

import dev.memory.common.validation.ValidationGroups;
import dev.memory.common.validation.ValidationGroups.NotBlankGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertCreateRequest {

    @NotBlank(message = "콘서트 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "콘서트 가격을 입력해주세요.")
    @Min(value = 10, message = "최소 가격은 10원 이상입니다.")
    private Integer price;

    @Valid
    @NotEmpty(message = "콘서트 일정을 추가해주세요.")
    private List<ConcertScheduleCreateRequest> schedules = new ArrayList<>();

    @AssertTrue(message = "가격은 10원 단위로만 입력 가능합니다.")
    public boolean isPriceValid() {
        if (price == null) return true;
        return price % 10 == 0;
    }
}
