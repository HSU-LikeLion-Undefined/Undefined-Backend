package com.likelion.RePlay.domain.user.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyMyPageDto {
    String nickName;
    Long year;
    String phoneId;
    String profileImage;
}
