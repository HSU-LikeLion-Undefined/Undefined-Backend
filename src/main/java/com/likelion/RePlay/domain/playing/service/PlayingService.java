package com.likelion.RePlay.domain.playing.service;

import com.likelion.RePlay.domain.learning.web.dto.MentorReviewRequestDto;
import com.likelion.RePlay.domain.playing.web.dto.*;
import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface PlayingService {
    ResponseEntity<CustomAPIResponse<?>> writePost(PlayingWriteRequestDTO postWriteRequestDTO, MultipartFile playingImage, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> modifyPost(Long playingId, PlayingWriteRequestDTO playingWriteRequestDTO, MultipartFile playingImage, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> getAllPosts();

    ResponseEntity<CustomAPIResponse<?>> getPost(Long playingId);

    ResponseEntity<CustomAPIResponse<?>> filtering(PlayingFilteringDTO playingFilteringDTO);

    ResponseEntity<CustomAPIResponse<?>> recruitPlaying(Long playingId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> cancelPlaying(Long playingId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> cancelScrap(Long playingId, MyUserDetailsService.MyUserDetails userDetails);


    ResponseEntity<CustomAPIResponse<?>> getMyPlayings(MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> deleteMyPlaying(Long playingId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> completePlaying(Long playingId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> recruitedPlayings(MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> writePlayingReview(PlayingReviewRequestDto playingReviewRequestDto, MyUserDetailsService.MyUserDetails userDetails);

    // 스크랩하기
    ResponseEntity<CustomAPIResponse<?>> scrapPlayings(Long playingId, MyUserDetailsService.MyUserDetails userDetails);

    // 스크랩한 게시물 조회
    ResponseEntity<CustomAPIResponse<?>> getMyScrapPlaying(MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> commentPlaying(Long playingId, PlayingCommentWriteRequestDTO playingCommentWriteRequestDTO, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> getAllComments(Long playingId);

    ResponseEntity<CustomAPIResponse<?>> deleteComment(Long commentId, MyUserDetailsService.MyUserDetails userDetails);

    // 작성자에 대한 후기 조회
    ResponseEntity<CustomAPIResponse<?>> getUserReview(UserReviewRequestDto userReviewRequestDto);


    ResponseEntity<CustomAPIResponse<?>> getApplicant(Long playingId, MyUserDetailsService.MyUserDetails userDetails);
}