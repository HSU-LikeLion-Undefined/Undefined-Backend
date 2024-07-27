package com.likelion.RePlay.domain.info.web.dto;

import com.likelion.RePlay.domain.info.entity.Info;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOneInfoResponseDto implements Serializable {
    private Long infoId; // 정보글 대표 번호
    private String title; //제목
    private String content; //내용
    private String writer; // 작성자 (관리자 이름)
    private Long infoNum; //생생정보터 호 수
    private String thumbnailUrl; // 대표 이미지 url
    private LocalDateTime createdAt; // 작성 날짜

    public static GetOneInfoResponseDto toEntity(Info info) {
        return GetOneInfoResponseDto.builder()
                .infoId(info.getInfoId())
                .title(info.getTitle())
                .content(info.getContent())
                .writer(info.getWriter())
                .infoNum(info.getInfoNum())
                .thumbnailUrl(info.getImages().isEmpty() ? null : info.getImages().get(0).getImageUrl())
                .createdAt(info.getCreatedAt())
                .build();
    }
}