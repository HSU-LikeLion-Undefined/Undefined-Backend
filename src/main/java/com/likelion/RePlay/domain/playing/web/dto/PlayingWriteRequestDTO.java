package com.likelion.RePlay.domain.playing.web.dto;


import com.likelion.RePlay.global.enums.Category;
import com.likelion.RePlay.global.enums.District;
import com.likelion.RePlay.global.enums.State;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayingWriteRequestDTO {
//    @NotNull(message = "회원 아이디는 비워져 있을 수 없습니다.")
//    private String phoneId; // 게시글 작성자 아이디
    private String introduce; // 자기소개
    private String title;
    private String date;
    private String locate; // 카카오맵으로 검색한 주소
    private double latitude; // 카카오맵에서 받아온 위도
    private double longitude; // 카카오맵에서 받아온 경도
    private State state;
    private District district;
    private Category category;
    private String content; // 놀이 설명
    private Long totalCount;
    private String cost; // 참가비용
    private String costDescription; //참가비용 설명
    private String imageUrl;
}
