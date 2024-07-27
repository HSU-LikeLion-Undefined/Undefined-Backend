package com.likelion.RePlay.domain.learning.web.controller;

import com.likelion.RePlay.domain.learning.service.LearningService;
import com.likelion.RePlay.domain.learning.web.dto.LearningApplyScrapRequestDTO;
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

    @GetMapping("/")
    private ResponseEntity<CustomAPIResponse<?>> getAllPosts() {
        return learningService.getAllPosts();
    }

    @GetMapping("/{learningId}")
    private ResponseEntity<CustomAPIResponse<?>> getPost(@PathVariable Long learningId) {
        return learningService.getPost(learningId);
    }

    @PostMapping("/filtering")
    private ResponseEntity<CustomAPIResponse<?>> filtering(@RequestBody LearningFilteringDTO learningFilteringDTO) {
        return learningService.filtering(learningFilteringDTO);
    }

    @PostMapping("/{learningId}")
    private ResponseEntity<CustomAPIResponse<?>> recruitLearning(@PathVariable Long learningId, @RequestBody LearningApplyScrapRequestDTO learningApplyScrapRequestDTO) {
        return learningService.recruitLearning(learningId, learningApplyScrapRequestDTO);
    }

    @DeleteMapping("/{learningId}")
    private ResponseEntity<CustomAPIResponse<?>> cancelLearning(@PathVariable Long learningId, @RequestBody LearningApplyScrapRequestDTO learningApplyScrapRequestDTO) {
        return learningService.cancelLearning(learningId, learningApplyScrapRequestDTO);
    }

    @PostMapping("/{learningId}/scrap")
    private ResponseEntity<CustomAPIResponse<?>> scrapLearning(@PathVariable Long learningId, @RequestBody LearningApplyScrapRequestDTO learningApplyScrapRequestDTO) {
        return learningService.scrapLearning(learningId, learningApplyScrapRequestDTO);
    }
}
