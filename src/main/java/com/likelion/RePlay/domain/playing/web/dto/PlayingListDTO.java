package com.likelion.RePlay.domain.playing.web.dto;


import com.likelion.RePlay.global.enums.Category;
import com.likelion.RePlay.global.enums.District;
import com.likelion.RePlay.global.enums.IsCompleted;
import com.likelion.RePlay.global.enums.State;
import lombok.*;

import java.util.Date;
import java.util.List;

public class PlayingListDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlayingResponse {
        private String nickname;
        private String introduce;
        private Category category;
        private String title;
        private String content;
        private String locate;
        private State state;
        private District district;
        private Date date;
        private Long totalCount;
        private Long recruitmentCount;
        private Long cost;
        private String costDescription;
        private IsCompleted isCompleted;
        private String imageUrl;
    }

    @Getter @Setter
    @Builder
    @NoArgsConstructor
    public static class SearchPlayings {
        private List<PlayingResponse> playings;

        public SearchPlayings(List<PlayingResponse> playings) {
            this.playings = playings;
        }
    }
}
