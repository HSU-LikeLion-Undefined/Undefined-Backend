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
        private Category category;
        private String title;
        private State state;
        private District district;
        private Date date;
        private Long totalCount;
        private Long recruitmentCount;
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
