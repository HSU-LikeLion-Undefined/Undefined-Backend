package com.likelion.RePlay.domain.learning.web.dto;

import com.likelion.RePlay.global.enums.Category;
import com.likelion.RePlay.global.enums.District;
import com.likelion.RePlay.global.enums.State;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningCommentWriteRequestDTO {
    private Long parentCommentId;
    private String content;
    private String date;
}
