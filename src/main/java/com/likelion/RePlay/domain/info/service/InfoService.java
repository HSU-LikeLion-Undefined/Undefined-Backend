package com.likelion.RePlay.domain.info.service;


import com.likelion.RePlay.domain.info.web.dto.InfoCreateDto;
import com.likelion.RePlay.domain.info.web.dto.InfoModifyDto;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InfoService {
    // 관리자가 생생정보터에 글 작성
    ResponseEntity<CustomAPIResponse<?>> createInfo(InfoCreateDto.InfoCreateRequest infoCreateRequest, List<MultipartFile> images);

    // 관리자가 생생정보터 게시글 수정
    ResponseEntity<CustomAPIResponse<?>> modifyInfo(InfoModifyDto.InfoModifyRequest infoModifyRequest, List<MultipartFile> images);

    // 관리자가 생생정보터 게시글 삭제 -> 보류
    ResponseEntity<CustomAPIResponse<?>> deleteInfo(Long infoId);
}
