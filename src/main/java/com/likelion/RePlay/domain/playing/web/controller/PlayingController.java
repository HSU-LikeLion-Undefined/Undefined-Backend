package com.likelion.RePlay.domain.playing.web.controller;

import com.likelion.RePlay.domain.learning.web.dto.MentorReviewRequestDto;
import com.likelion.RePlay.domain.playing.web.dto.*;
import com.likelion.RePlay.domain.playing.service.PlayingServiceImpl;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/playing")
@RequiredArgsConstructor
public class PlayingController {

    private final PlayingServiceImpl playingService;

    @PostMapping(value = "/writePost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private ResponseEntity<CustomAPIResponse<?>> writePost(
            @RequestPart("playingWriteRequestDTO") PlayingWriteRequestDTO playingWriteRequestDTO,
            @RequestPart(value = "playingImage", required = false) MultipartFile playingImage,
            @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {

        return playingService.writePost(playingWriteRequestDTO, playingImage, userDetails);
    }

    @PutMapping("/{playingId}")
    private ResponseEntity<CustomAPIResponse<?>> modifyPost(
            @PathVariable Long playingId,
            @RequestPart("playingWriteRequestDTO") PlayingWriteRequestDTO playingWriteRequestDTO,
            @RequestPart(value = "playingImage", required = false) MultipartFile playingImage,
            @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.modifyPost(playingId, playingWriteRequestDTO, playingImage, userDetails);
    }

    @GetMapping("/getPlayings")
    private ResponseEntity<CustomAPIResponse<?>> getAllPosts() {
        return playingService.getAllPosts();
    }

    @GetMapping("/getPlayings/{playingId}")
    private ResponseEntity<CustomAPIResponse<?>> getPost(@PathVariable Long playingId) {
        return playingService.getPost(playingId);
    }

    @PostMapping("/filtering")
    private ResponseEntity<CustomAPIResponse<?>> filtering(@RequestBody PlayingFilteringDTO playingFilteringDTO) {
        return playingService.filtering(playingFilteringDTO);
    }

    @PostMapping("/{playingId}")
    private ResponseEntity<CustomAPIResponse<?>> recruitPlaying(@PathVariable Long playingId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.recruitPlaying(playingId, userDetails);
    }

    @DeleteMapping("/{playingId}")
    private ResponseEntity<CustomAPIResponse<?>> cancelPlaying(@PathVariable Long playingId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.cancelPlaying(playingId, userDetails);
    }


    @PostMapping("/{playingId}/scrap")
    private ResponseEntity<CustomAPIResponse<?>> scrapPlaying(@PathVariable Long playingId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.scrapPlayings(playingId, userDetails);
    }

    // 스크랩 취소
    @DeleteMapping("/{playingId}/scrap")
    private ResponseEntity<CustomAPIResponse<?>> cancelScrap(@PathVariable Long playingId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.cancelScrap(playingId, userDetails);
    }

    // 작성 놀이터 조회
    @GetMapping("/myPlayings")
    private ResponseEntity<CustomAPIResponse<?>> getMyPlayings(@AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.getMyPlayings(userDetails);
    }

    // 내가 만든 놀이터 삭제
    @DeleteMapping("/myPlayings/{playingId}")
    private ResponseEntity<CustomAPIResponse<?>> deleteMyPlaying(@PathVariable Long playingId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.deleteMyPlaying(playingId, userDetails);
    }

    @PostMapping("/{playingId}/complete")
    private ResponseEntity<CustomAPIResponse<?>> completePlaying(@PathVariable Long playingId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.completePlaying(playingId, userDetails);
    }

    @GetMapping("/recruited")
    private ResponseEntity<CustomAPIResponse<?>> recruitedPlayings(@AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.recruitedPlayings(userDetails);
    }


    // 리뷰 작성
    @PostMapping("/writeReview")
    private ResponseEntity<CustomAPIResponse<?>> writeReviewPlayings(
            @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails,
            @RequestBody PlayingReviewRequestDto playingReviewRequestDto) {
        return playingService.writePlayingReview(playingReviewRequestDto, userDetails);
    }

    // 스크랩한 게시물 가져오기
    @GetMapping("/scrap")
    private ResponseEntity<CustomAPIResponse<?>> scrapPlaying(@AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.getMyScrapPlaying(userDetails);
    }

    @PostMapping("/{playingId}/comment")
    private ResponseEntity<CustomAPIResponse<?>> commentPlaying(@PathVariable Long playingId, @RequestBody PlayingCommentWriteRequestDTO playingCommentWriteRequestDTO, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.commentPlaying(playingId, playingCommentWriteRequestDTO, userDetails);
    }

    @GetMapping("/comment/{playingId}")
    private ResponseEntity<CustomAPIResponse<?>> getAllComments(@PathVariable Long playingId) {
        return playingService.getAllComments(playingId);
    }

    @DeleteMapping("/{commentId}/comment")
    private ResponseEntity<CustomAPIResponse<?>> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.deleteComment(commentId, userDetails);
    }

    @PostMapping("/getUserReview")
    private ResponseEntity<CustomAPIResponse<?>> getUserReview(
            @RequestBody UserReviewRequestDto userReviewRequestDto){
        return playingService.getUserReview(userReviewRequestDto);
    }

    // 신청자 조회
    @GetMapping("/getApplicant/{playingId}")
    private ResponseEntity<CustomAPIResponse<?>> getApplicant(@PathVariable Long playingId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.getApplicant(playingId, userDetails);
    }


}