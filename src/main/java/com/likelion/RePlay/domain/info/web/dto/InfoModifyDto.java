package com.likelion.RePlay.domain.info.web.dto;

import com.likelion.RePlay.domain.info.entity.Info;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

public class InfoModifyDto implements Serializable {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InfoModifyRequest {
        @NotNull(message = "ID를 입력해주세요.")
        private Long infoId; // 수정할 글의 ID

        @NotBlank(message = "제목을 입력해주세요.")
        private String title; // 수정할 제목

        @NotBlank(message = "내용을 입력해주세요.")
        private String content; // 수정할 내용

        @NotNull(message="몇 호인지 입력해주세요.")
        private Long infoNum; //수정할 생생정보터 호 수

        public Info toEntity(){
            return Info.builder()
                    .title(title)
                    .content(content)
                    .infoNum(infoNum)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyInfo {
        private LocalDateTime updatedAt;
    }
}