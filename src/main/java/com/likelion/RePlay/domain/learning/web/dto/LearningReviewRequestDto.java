package com.likelion.RePlay.domain.learning.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningReviewRequestDto {
    @NotNull(message = "후기를 남길 게시글 ID를 입력해주세요.")
    private Long learningId;

    // 내용
    private String content;

    // 별점
    @NotNull(message = "평점을 입력해주세요.")
    private Double rate;
}