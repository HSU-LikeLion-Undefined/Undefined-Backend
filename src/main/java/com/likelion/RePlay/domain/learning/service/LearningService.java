package com.likelion.RePlay.domain.learning.service;

import com.likelion.RePlay.domain.learning.web.dto.LearningCommentWriteRequestDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningFilteringDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningReviewRequestDto;
import com.likelion.RePlay.domain.learning.web.dto.LearningWriteRequestDTO;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface LearningService {
    ResponseEntity<CustomAPIResponse<?>> writePost(LearningWriteRequestDTO learningWriteRequestDTO, MultipartFile learningImage, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> modifyPost(Long learningId, LearningWriteRequestDTO learningWriteRequestDTO, MultipartFile learningImage, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> getAllPosts();

    ResponseEntity<CustomAPIResponse<?>> getPost(Long learningId);

    ResponseEntity<CustomAPIResponse<?>> filtering(LearningFilteringDTO learningFilteringDTO);

    ResponseEntity<CustomAPIResponse<?>> recruitLearning(Long learningId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> cancelLearning(Long learningId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> cancelScrap(Long learningId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> recruitedLearnings(MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> writeLearningReview(LearningReviewRequestDto learningReviewRequestDto, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> completeLearning(Long learningId, MyUserDetailsService.MyUserDetails userDetails);

    // 배움글 스크랩
    ResponseEntity<CustomAPIResponse<?>> scrapLearning(Long learningId, MyUserDetailsService.MyUserDetails userDetails);

    // 스크랩한 배움글을 가져옴
    ResponseEntity<CustomAPIResponse<?>> getMyScrapLearnings(MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> commentLearning(Long learningId, LearningCommentWriteRequestDTO learningCommentWriteRequestDTO, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> getAllComments(Long learningId);

    ResponseEntity<CustomAPIResponse<?>> deleteComment(Long commentId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> getMentorReview(String mentorName);

}
