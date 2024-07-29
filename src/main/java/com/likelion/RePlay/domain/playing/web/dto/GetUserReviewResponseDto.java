package com.likelion.RePlay.domain.playing.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class GetUserReviewResponseDto {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FinalResponseDto{
        private List<GetUserReviewResponseDto.GetReview> reviewList;

        // 평점
        private Double averageRate;

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetReview {
        // 작성자 닉네임
        private String writer;

        // 한줄평
        private String content;

        // 별점
        private Double rate;
    }
}

