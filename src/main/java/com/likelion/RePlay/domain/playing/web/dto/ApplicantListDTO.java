package com.likelion.RePlay.domain.playing.web.dto;

import lombok.*;

import java.util.List;

public class ApplicantListDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicantResponse {
        private String nickname;
        private String phoneId;
        private String profileImage;
    }

    @Getter @Setter
    @Builder
    @NoArgsConstructor
    public static class SearchApplicant {
        private List<ApplicantResponse> applicantResponses;

        public SearchApplicant(List<ApplicantResponse> applicantResponses) {
            this.applicantResponses = applicantResponses;
        }
    }
}
