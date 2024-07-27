package com.likelion.RePlay.domain.info.web.controller;

import com.likelion.RePlay.domain.info.service.InfoService;
import com.likelion.RePlay.domain.info.web.dto.GetAllInfoResponseDto;
import com.likelion.RePlay.domain.info.web.dto.InfoCreateDto;
import com.likelion.RePlay.domain.info.web.dto.InfoModifyDto;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/info")
@RequiredArgsConstructor
public class InfoController {
    private final InfoService infoService;

    @PostMapping("/writeInfo")
    public ResponseEntity<CustomAPIResponse<?>> writeInfo(
            @RequestPart("infoCreateRequest") InfoCreateDto.InfoCreateRequest infoCreateRequest,
            @RequestPart("images") List<MultipartFile> images) {
        return infoService.createInfo(infoCreateRequest, images);
    }

    @PostMapping("/modifyInfo")
    public ResponseEntity<CustomAPIResponse<?>> modifyInfo(
            @RequestPart("infoModifyRequest") InfoModifyDto.InfoModifyRequest infoModifyRequest,
            @RequestPart("images") List<MultipartFile> images){
        return infoService.modifyInfo(infoModifyRequest, images);
    }

    @GetMapping("/getAllInfo")
    public ResponseEntity<CustomAPIResponse<GetAllInfoResponseDto.FinalResponseDto>> getAllInfo() {
        return infoService.getAllInfo();
    }
}