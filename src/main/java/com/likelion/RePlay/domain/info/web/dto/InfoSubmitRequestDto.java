package com.likelion.RePlay.domain.info.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public class InfoSubmitRequestDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class InfoSubmitRequest implements Serializable {
        @NotBlank(message = "제목을 입력해주세요.")
        private String title; //제목

        @NotBlank(message = "내용을 입력해주세요.")
        private String content; //내용

        private String writer; // 작성자 (작성자 이름)

        private List<MultipartFile> images; //이미지
    }
}