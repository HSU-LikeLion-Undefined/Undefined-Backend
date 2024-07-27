package com.likelion.RePlay.domain.learning.service;

import com.likelion.RePlay.domain.learning.web.dto.LearningApplyRequestDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningFilteringDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningWriteRequestDTO;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import org.springframework.http.ResponseEntity;

public interface LearningService {
    ResponseEntity<CustomAPIResponse<?>> writePost(LearningWriteRequestDTO learningWriteRequestDTO);

    ResponseEntity<CustomAPIResponse<?>> getAllPosts();

    ResponseEntity<CustomAPIResponse<?>> getPost(Long learningId);

    ResponseEntity<CustomAPIResponse<?>> filtering(LearningFilteringDTO learningFilteringDTO);

    ResponseEntity<CustomAPIResponse<?>> recruitLearning(Long learningId, LearningApplyRequestDTO learningApplyRequestDTO);
}
