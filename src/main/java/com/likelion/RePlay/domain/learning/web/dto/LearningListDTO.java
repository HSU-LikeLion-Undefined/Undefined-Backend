package com.likelion.RePlay.domain.learning.web.dto;


import com.likelion.RePlay.global.enums.Category;
import com.likelion.RePlay.global.enums.District;
import com.likelion.RePlay.global.enums.IsCompleted;
import com.likelion.RePlay.global.enums.State;
import lombok.*;

import java.util.Date;
import java.util.List;

public class LearningListDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LearningResponse {
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

        // 멘토 추가
        private String mentorName;
    }

    @Getter @Setter
    @Builder
    @NoArgsConstructor
    public static class SearchLearnings {
        private List<LearningResponse> learnings;

        public SearchLearnings(List<LearningResponse> learnings) {
            this.learnings = learnings;
        }
    }
}
