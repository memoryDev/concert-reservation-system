package dev.memory.member.dto;

import dev.memory.member.domain.Member;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberJoinRequest {

    @Pattern(
            regexp = "^[a-zA-Z0-9_-]+$",
            message = "영문 대소문자, 숫자, 하이픈(-), 언더바(_)만 사용할 수 있어요."
    )
    @Size(min = 5, max = 20, message = "아이디는 5~20자 사이로 입력해 주세요.")
    private String userId;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Size(min = 5, message = "비밀번호는 최소 5자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "활동할 때 사용할 닉네임을 정해 주세요.")
    @Size(min = 2, max = 10, message = "닉네임은 2~10자 이내로 만들어 주세요.")
    private String nickname;
}
