package com.likelion.RePlay.domain.playing.web.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayingCommentWriteRequestDTO {
    private Long parentCommentId;
    private String content;
    private String date;

}
