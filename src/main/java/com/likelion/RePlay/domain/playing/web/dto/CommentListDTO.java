package com.likelion.RePlay.domain.playing.web.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

public class CommentListDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentResponse {
        private String nickname;
        private String content;
        private Date date;
        private Long commentId;
        private Long parentCommentId;

    }

    @Getter @Setter
    @Builder
    @NoArgsConstructor
    public static class SearchComment {
        private List<CommentResponse> commentResponses;

        public SearchComment(List<CommentResponse> commentResponses) {
            this.commentResponses = commentResponses;
        }
    }

}
