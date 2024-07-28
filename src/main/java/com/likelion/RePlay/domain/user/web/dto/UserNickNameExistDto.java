package com.likelion.RePlay.domain.user.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserNickNameExistDto {
    @NotEmpty(message = "닉네임이 필요합니다.")
    String nickName;
}
