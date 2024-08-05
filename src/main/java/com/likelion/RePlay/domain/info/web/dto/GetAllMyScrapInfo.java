package com.likelion.RePlay.domain.info.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class GetAllMyScrapInfo {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FinalResponseDto {
        private List<GetInfo> infoScraps;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetInfo {
        private Long infoId; // 정보글 대표 번호
        private String title; //제목
        private String content; //내용
        private String writer; // 작성자 (관리자 이름)
        private Long infoNum; //생생정보터 호 수
        private List<String> thumbnailUrl; // 대표 이미지 url
        private LocalDate createdAt; // 작성 날짜
    }
}