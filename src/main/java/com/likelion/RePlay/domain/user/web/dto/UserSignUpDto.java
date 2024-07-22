package com.likelion.RePlay.domain.user.web.dto;

import com.likelion.RePlay.global.enums.District;
import com.likelion.RePlay.global.enums.State;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSignUpDto {
    @NotEmpty(message = "전화번호가 필요합니다.")
    private String phoneId;

    @NotEmpty(message = "비밀번호가 필요합니다.")
    private String password;

    @NotEmpty(message = "닉네임이 필요합니다.")
    private String nickname;

    @NotEmpty(message = "주소 (시)가 필요합니다.")
    private State state;

    @NotEmpty(message= "주소 (구)가 필요합니다.")
    private District district;

    @NotEmpty(message = "출생연도가 필요합니다.")
    private Long year;
}