package com.likelion.RePlay.domain.learning.web.controller;

import com.likelion.RePlay.domain.learning.service.LearningService;
import com.likelion.RePlay.domain.learning.web.dto.LearningApplyRequestDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningFilteringDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningWriteRequestDTO;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/learning")
@RequiredArgsConstructor
public class LearningController {

    private final LearningService learningService;

    @PostMapping("/writePost")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    private ResponseEntity<CustomAPIResponse<?>> writePost(@RequestBody LearningWriteRequestDTO learningWriteRequestDTO) {
        return learningService.writePost(learningWriteRequestDTO);
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
    private ResponseEntity<CustomAPIResponse<?>> recruitLearning(@PathVariable Long learningId, @RequestBody LearningApplyRequestDTO learningApplyRequestDTO) {
        return learningService.recruitLearning(learningId, learningApplyRequestDTO);
    }
}
