package com.likelion.RePlay.domain.user.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserPageResponseDto {
    String phoneId;
    String nickName;
    Long year;
}
