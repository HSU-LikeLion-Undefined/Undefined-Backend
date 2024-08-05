package com.likelion.RePlay.domain.info.web.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoSubmitResponseDto {
    private String title;
    private String content;
    private String writer;
}
