package com.likelion.RePlay.domain.info.service;

import com.likelion.RePlay.domain.info.entity.Info;
import com.likelion.RePlay.domain.info.web.dto.*;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService.MyUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InfoService {
    ResponseEntity<CustomAPIResponse<?>> createInfo(InfoCreateDto.InfoCreateRequest infoCreateRequest, List<MultipartFile> images, MyUserDetails userDetails);
    ResponseEntity<CustomAPIResponse<?>> modifyInfo(InfoModifyDto.InfoModifyRequest infoModifyRequest, List<MultipartFile> images, MyUserDetails userDetails);
    ResponseEntity<CustomAPIResponse<GetAllInfoResponseDto.FinalResponseDto>> getAllInfo();
    ResponseEntity<CustomAPIResponse<?>> getOneInfo(Long infoId);
    ResponseEntity<CustomAPIResponse<?>> submitInfo(InfoSubmitRequestDto.InfoSubmitRequest infoSubmitRequest, List<MultipartFile> images, MyUserDetails userDetails);
    ResponseEntity<CustomAPIResponse<?>> scrapInfo(InfoScrapDto infoScrapDto, MyUserDetails userDetails);

    // 내가 스크랩한 정보글 조회
    ResponseEntity<CustomAPIResponse<?>> getMyScrapInfo(MyUserDetails userDetails);
}