package com.likelion.RePlay.domain.info.web.controller;

import com.likelion.RePlay.domain.info.service.InfoService;
import com.likelion.RePlay.domain.info.web.dto.GetAllInfoResponseDto;
import com.likelion.RePlay.domain.info.web.dto.InfoCreateDto;
import com.likelion.RePlay.domain.info.web.dto.InfoModifyDto;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/info")
@RequiredArgsConstructor
public class InfoController {
    private final InfoService infoService;

    @PostMapping("/createInfo")
    public ResponseEntity<CustomAPIResponse<?>> createInfo(
            @RequestPart("infoCreateRequest") InfoCreateDto.InfoCreateRequest infoRequest,
            @RequestPart("images") List<MultipartFile> images,
            @AuthenticationPrincipal MyUserDetails userDetails) {
        return infoService.createInfo(infoRequest, images, userDetails);
    }

    @PostMapping("/modifyInfo")
    public ResponseEntity<CustomAPIResponse<?>> modifyInfo(
            @RequestPart("infoModifyRequest") InfoModifyDto.InfoModifyRequest infoModifyRequest,
            @RequestPart("images") List<MultipartFile> images) {
        return infoService.modifyInfo(infoModifyRequest, images);
    }

    @GetMapping("/getAllInfo")
    public ResponseEntity<CustomAPIResponse<GetAllInfoResponseDto.FinalResponseDto>> getAllInfo() {
        return infoService.getAllInfo();
    }
}