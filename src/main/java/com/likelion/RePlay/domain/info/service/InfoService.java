package com.likelion.RePlay.domain.info.service;


import com.likelion.RePlay.global.response.CustomAPIResponse;
import org.springframework.http.ResponseEntity;

public interface InfoService {
    // 관리자가 생생정보터에 글 작성
    ResponseEntity<CustomAPIResponse<?>> createInfo(CreateInfoDto createInfoDto);
}
