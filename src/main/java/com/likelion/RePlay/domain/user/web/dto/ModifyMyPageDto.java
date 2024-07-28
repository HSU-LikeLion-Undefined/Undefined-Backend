package com.likelion.RePlay.domain.user.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyMyPageDto {
    @NotEmpty(message = "닉네임이 필요합니다.")
    String nickName;

    @NotNull(message = "출생연도가 필요합니다.")
    Long year;

    @NotEmpty(message = "전화번호가 필요합니다.")
    String phoneId;

    String profileImage;
}
