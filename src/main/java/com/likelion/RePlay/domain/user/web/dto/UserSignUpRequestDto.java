package com.likelion.RePlay.domain.user.web.dto;

import com.likelion.RePlay.global.enums.District;
import com.likelion.RePlay.global.enums.State;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserSignUpRequestDto {
    @NotEmpty(message = "전화번호가 필요합니다.")
    private String phoneId;

    @NotEmpty(message = "비밀번호가 필요합니다.")
    private String password;

    @NotEmpty(message = "닉네임이 필요합니다.")
    private String nickname;

    //@NotNull(message = "주소 (시)가 필요합니다.")
    private State state;

    //@NotNull(message= "주소 (구)가 필요합니다.")
    private District district;

    @NotNull(message = "출생연도가 필요합니다.")
    private Long year;

    @NotNull(message = "사용자 역할이 필요합니다.")
    List<String> userRoles;
}