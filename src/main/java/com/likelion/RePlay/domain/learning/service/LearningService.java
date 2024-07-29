package com.likelion.RePlay.domain.learning.service;

import com.likelion.RePlay.domain.learning.web.dto.LearningCommentWriteRequestDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningFilteringDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningWriteRequestDTO;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

public interface LearningService {
    ResponseEntity<CustomAPIResponse<?>> writePost(LearningWriteRequestDTO learningWriteRequestDTO, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> modifyPost(Long learningId, LearningWriteRequestDTO learningWriteRequestDTO, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> getAllPosts();

    ResponseEntity<CustomAPIResponse<?>> getPost(Long learningId);

    ResponseEntity<CustomAPIResponse<?>> filtering(LearningFilteringDTO learningFilteringDTO);

    ResponseEntity<CustomAPIResponse<?>> recruitLearning(Long learningId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> cancelLearning(Long learningId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> scrapLearning(Long learningId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> cancelScrap(Long learningId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> recruitedLearnings(MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> scrapLearnings(MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> commentLearning(Long learningId, LearningCommentWriteRequestDTO learningCommentWriteRequestDTO, MyUserDetailsService.MyUserDetails userDetails);
}
