package com.likelion.RePlay.domain.playing.web.dto;


import com.likelion.RePlay.global.enums.Category;
import com.likelion.RePlay.global.enums.District;
import com.likelion.RePlay.global.enums.State;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayingWriteRequestDTO {

    private String introduce; // 자기소개

    @NotEmpty(message = "제목은 비워져 있을 수 없습니다.")
    private String title;

    @NotEmpty(message = "날짜는 비워져 있을 수 없습니다.")
    private String date;

    @NotEmpty(message = "주소는 비워져 있을 수 없습니다.")
    private String locate; // 카카오맵으로 검색한 주소

    private double latitude; // 카카오맵에서 받아온 위도
    private double longitude; // 카카오맵에서 받아온 경도

    @NotNull(message = "시는 비워져 있을 수 없습니다.")
    private State state;

    @NotNull(message = "구는 비워져 있을 수 없습니다.")
    private District district;

    @NotNull(message = "카테고리는 비워져 있을 수 없습니다.")
    private Category category;

    private String content; // 놀이 설명

    @NotNull(message = "총 인원은 비워져 있을 수 없습니다.")
    @Positive(message = "총 인원은 양수여야 합니다.")
    private Long totalCount;

    @NotNull(message = "참가 비용은 비워져 있을 수 없습니다.")
    @Positive(message = "참가 비용은 양수여야 합니다.")
    private Long cost; // 참가비용

    private String costDescription; // 참가비용 설명
    private String playingImage;
}
