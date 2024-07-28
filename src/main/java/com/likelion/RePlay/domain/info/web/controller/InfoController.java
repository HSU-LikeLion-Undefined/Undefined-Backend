package com.likelion.RePlay.domain.info.web.controller;

import com.likelion.RePlay.domain.info.entity.Info;
import com.likelion.RePlay.domain.info.service.InfoService;
import com.likelion.RePlay.domain.info.web.dto.*;
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
            @RequestPart("images") List<MultipartFile> images,
            @AuthenticationPrincipal MyUserDetails userDetails) {
        return infoService.modifyInfo(infoModifyRequest, images, userDetails);
    }

    @GetMapping("/getAllInfo")
    public ResponseEntity<CustomAPIResponse<GetAllInfoResponseDto.FinalResponseDto>> getAllInfo() {
        return infoService.getAllInfo();
    }

    @GetMapping("/getOneInfo/{infoId}")
    public ResponseEntity<CustomAPIResponse<?>> getOneInfo(@PathVariable Long infoId) {
        return infoService.getOneInfo(infoId);
    }

    @PostMapping("/submitInfo")
    public ResponseEntity<CustomAPIResponse<?>> submitInfo(
            @RequestPart("infoSubmitRequest") InfoSubmitRequestDto.InfoSubmitRequest infoSubmitRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @AuthenticationPrincipal MyUserDetails userDetails) {
        return infoService.submitInfo(infoSubmitRequest, images, userDetails);
    }

    @PostMapping("/scrapInfo")
    public ResponseEntity<CustomAPIResponse<?>> scrapInfo(@RequestBody InfoScrapDto infoScrapDto, @AuthenticationPrincipal MyUserDetails userDetails) {
        return infoService.scrapInfo(infoScrapDto, userDetails);
    }

    @GetMapping("/getMyScrapInfo")
    public ResponseEntity<CustomAPIResponse<?>> getMyScrapInfo(@AuthenticationPrincipal MyUserDetails userDetails) {
        return infoService.getMyScrapInfo(userDetails);
    }
}
