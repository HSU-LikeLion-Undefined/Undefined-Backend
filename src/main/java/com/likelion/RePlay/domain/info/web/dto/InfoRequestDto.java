package com.likelion.RePlay.domain.info.web.dto;

import com.likelion.RePlay.global.enums.RoleName;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoRequestDto {
    private RoleName roleName; //권한
    private String title; //제목
    private String content; //내용
    private Long infoNum; //생생정보터 호 수
}
