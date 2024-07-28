package com.likelion.RePlay.domain.user.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSignUpResponseDto {
    private String nickname;
    private String phoneId;
    private String password;

}
