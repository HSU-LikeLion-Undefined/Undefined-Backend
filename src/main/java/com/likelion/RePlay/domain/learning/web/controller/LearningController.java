package com.likelion.RePlay.domain.learning.web.controller;

import com.amazonaws.Response;
import com.likelion.RePlay.domain.learning.service.LearningService;
import com.likelion.RePlay.domain.learning.web.dto.LearningCommentWriteRequestDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningFilteringDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningWriteRequestDTO;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
public class LearningController {

    private final LearningService learningService;

    @PostMapping("/writePost")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    private ResponseEntity<CustomAPIResponse<?>> writePost(@RequestBody LearningWriteRequestDTO learningWriteRequestDTO, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.writePost(learningWriteRequestDTO, userDetails);
    }

    @PutMapping("/{learningId}")
    private ResponseEntity<CustomAPIResponse<?>> modifyPost(@PathVariable Long learningId, @RequestBody LearningWriteRequestDTO learningWriteRequestDTO, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.modifyPost(learningId, learningWriteRequestDTO, userDetails);
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

    @PostMapping("/{learningId}/scrap")
    private ResponseEntity<CustomAPIResponse<?>> scrapLearning(@PathVariable Long learningId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.scrapLearning(learningId, userDetails);
    }

    @DeleteMapping("/{learningId}/scrap")
    private ResponseEntity<CustomAPIResponse<?>> cancelScrap(@PathVariable Long learningId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.cancelScrap(learningId, userDetails);
    }

    @GetMapping("/recruited")
    private ResponseEntity<CustomAPIResponse<?>> recruitedLearnings(@AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.recruitedLearnings(userDetails);
    }

    @GetMapping("/scrap")
    private ResponseEntity<CustomAPIResponse<?>> scrapLearnings(@AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.scrapLearnings(userDetails);
    }

    @PostMapping("/{learningId}/comment")
    private ResponseEntity<CustomAPIResponse<?>> commentLearning(@PathVariable Long learningId, @RequestBody LearningCommentWriteRequestDTO learningCommentWriteRequestDTO, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return learningService.commentLearning(learningId, learningCommentWriteRequestDTO, userDetails);
    }

    @GetMapping("/comment/{learningId}")
    private ResponseEntity<CustomAPIResponse<?>> getAllComments(@PathVariable Long learningId) {
        return learningService.getAllComments(learningId);
    }

}
