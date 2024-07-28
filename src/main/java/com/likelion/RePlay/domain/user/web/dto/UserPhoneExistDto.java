package com.likelion.RePlay.domain.user.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoneExistDto {
    @NotEmpty(message = "전화번호가 필요합니다.")
    String phoneId;
}
