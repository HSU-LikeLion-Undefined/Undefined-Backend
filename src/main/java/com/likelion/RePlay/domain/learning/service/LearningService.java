package com.likelion.RePlay.domain.learning.service;

import com.likelion.RePlay.domain.learning.web.dto.LearningApplyScrapRequestDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningFilteringDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningWriteRequestDTO;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService;
import org.springframework.http.ResponseEntity;

public interface LearningService {
    ResponseEntity<CustomAPIResponse<?>> writePost(LearningWriteRequestDTO learningWriteRequestDTO, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> getAllPosts();

    ResponseEntity<CustomAPIResponse<?>> getPost(Long learningId);

    ResponseEntity<CustomAPIResponse<?>> filtering(LearningFilteringDTO learningFilteringDTO);

    ResponseEntity<CustomAPIResponse<?>> recruitLearning(Long learningId, LearningApplyScrapRequestDTO learningApplyScrapRequestDTO);

    ResponseEntity<CustomAPIResponse<?>> cancelLearning(Long learningId, LearningApplyScrapRequestDTO learningApplyScrapRequestDTO);

    ResponseEntity<CustomAPIResponse<?>> scrapLearning(Long learningId, LearningApplyScrapRequestDTO learningApplyScrapRequestDTO);

    ResponseEntity<CustomAPIResponse<?>> cancelScrap(Long learningId, LearningApplyScrapRequestDTO learningApplyScrapRequestDTO);
}
