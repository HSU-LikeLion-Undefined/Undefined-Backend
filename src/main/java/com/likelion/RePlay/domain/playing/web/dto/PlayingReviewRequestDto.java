package com.likelion.RePlay.domain.playing.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayingReviewRequestDto {
    // 놀이터 게시글 고유 번호
    @NotNull(message = "후기를 남길 게시글 ID를 입력해주세요.")
    private Long playingId;

    // 후기 내용
    private String content;

    // 별점
    @NotNull(message = "평점을 입력해주세요.")
    private Double rate;
}
