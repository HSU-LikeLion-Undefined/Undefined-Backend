package com.likelion.RePlay.playing.dto;

import com.likelion.RePlay.enums.Category;
import com.likelion.RePlay.enums.District;
import com.likelion.RePlay.enums.State;
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
        private State state;
        private District district;
        private Date date;
        private Long totalCount;
        private Long recruitmentCount;
        private Long cost;
        private String costDescription;
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
