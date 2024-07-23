package com.likelion.RePlay.playing.dto;

import com.likelion.RePlay.enums.Category;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayingWriteRequestDTO {
    @NotNull(message = "회원 아이디는 비워져 있을 수 없습니다.")
    private String phoneId; // 게시글 작성자 아이디
    private String introduce; // 자기소개
    private String title;
    private String date;
    private String locate;
    private Category category;
    private String content; // 놀이 설명
    private Long totalCount;
    private String cost; // 참가비용
    private String costDescription; //참가비용 설명
}
