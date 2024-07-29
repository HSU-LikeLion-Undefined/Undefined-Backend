package com.likelion.RePlay.domain.learning.web.controller;

import com.likelion.RePlay.domain.learning.service.LearningService;
import com.likelion.RePlay.domain.learning.web.dto.LearningCommentWriteRequestDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningFilteringDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningReviewRequestDto;
import com.likelion.RePlay.domain.learning.web.dto.LearningWriteRequestDTO;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
public class LearningController {

    private final LearningService learningService;

    @PostMapping("/writePost")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    private ResponseEntity<CustomAPIResponse<?>> writePost(
            @RequestPart("learningWriteRequestDTO") LearningWriteRequestDTO learningWriteRequestDTO,
            @RequestPart(value = "learningImage", required = false) MultipartFile learningImage,
            @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.writePost(learningWriteRequestDTO, learningImage, userDetails);
    }

    @PutMapping("/{learningId}")
    private ResponseEntity<CustomAPIResponse<?>> modifyPost(
            @PathVariable Long learningId,
            @RequestPart("learningWriteRequestDTO") LearningWriteRequestDTO learningWriteRequestDTO,
            @RequestPart(value = "learningImage", required = false) MultipartFile learningImage,
            @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.modifyPost(learningId, learningWriteRequestDTO, learningImage, userDetails);
    }

    @GetMapping("/getLearnings")
    private ResponseEntity<CustomAPIResponse<?>> getAllPosts() {
        return learningService.getAllPosts();
    }

    @GetMapping("/getLearnings/{learningId}")
    private ResponseEntity<CustomAPIResponse<?>> getPost(@PathVariable Long learningId) {
        return learningService.getPost(learningId);
    }

    @PostMapping("/filtering")
    private ResponseEntity<CustomAPIResponse<?>> filtering(@RequestBody LearningFilteringDTO learningFilteringDTO) {
        return learningService.filtering(learningFilteringDTO);
    }

    @PostMapping("/{learningId}")
    private ResponseEntity<CustomAPIResponse<?>> recruitLearning(@PathVariable Long learningId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.recruitLearning(learningId, userDetails);
    }

    @DeleteMapping("/{learningId}")
    private ResponseEntity<CustomAPIResponse<?>> cancelLearning(@PathVariable Long learningId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.cancelLearning(learningId, userDetails);
    }

    // 스크랩 설정
    @PostMapping("/{learningId}/scrap")
    private ResponseEntity<CustomAPIResponse<?>> scrapLearning(@PathVariable Long learningId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.scrapLearning(learningId,userDetails);
    }

    @DeleteMapping("/{learningId}/scrap")
    private ResponseEntity<CustomAPIResponse<?>> cancelScrap(@PathVariable Long learningId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.cancelScrap(learningId, userDetails);
    }

    @GetMapping("/recruited")
    private ResponseEntity<CustomAPIResponse<?>> recruitedLearnings(@AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.recruitedLearnings(userDetails);
    }


    @PostMapping("/writeReview")
    private ResponseEntity<CustomAPIResponse<?>> writeReviewLearnings(
            @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails,
            @RequestBody LearningReviewRequestDto learningReviewRequestDto) {
        return learningService.writeLearningReview(learningReviewRequestDto, userDetails);
    }

    @PostMapping("/{learningId}/complete")
    private ResponseEntity<CustomAPIResponse<?>> completeLearning(@PathVariable Long learningId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.completeLearning(learningId, userDetails);
    }

    // 스크랩 조회
    @GetMapping("/scrap")
    private ResponseEntity<CustomAPIResponse<?>> scrapLearnings(@AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.getMyScrapLearnings(userDetails);
    }

    @PostMapping("/{learningId}/comment")
    private ResponseEntity<CustomAPIResponse<?>> commentLearning(@PathVariable Long learningId, @RequestBody LearningCommentWriteRequestDTO learningCommentWriteRequestDTO, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.commentLearning(learningId, learningCommentWriteRequestDTO, userDetails);
    }

    @GetMapping("/comment/{learningId}")
    private ResponseEntity<CustomAPIResponse<?>> getAllComments(@PathVariable Long learningId) {
        return learningService.getAllComments(learningId);
    }

    @DeleteMapping("/{commentId}/comment")
    private ResponseEntity<CustomAPIResponse<?>> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.deleteComment(commentId, userDetails);

    }

}
