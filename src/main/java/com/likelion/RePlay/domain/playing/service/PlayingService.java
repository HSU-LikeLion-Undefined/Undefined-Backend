package com.likelion.RePlay.domain.playing.service;

import com.likelion.RePlay.domain.playing.web.dto.PlayingFilteringDTO;
import com.likelion.RePlay.domain.playing.web.dto.PlayingReviewRequestDto;
import com.likelion.RePlay.domain.playing.web.dto.PlayingWriteRequestDTO;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService;
import org.springframework.http.ResponseEntity;


public interface PlayingService {
    ResponseEntity<CustomAPIResponse<?>> writePost(PlayingWriteRequestDTO postWriteRequestDTO, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> modifyPost(Long playingId, PlayingWriteRequestDTO playingWriteRequestDTO, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> getAllPosts();

    ResponseEntity<CustomAPIResponse<?>> getPost(Long playingId);

    ResponseEntity<CustomAPIResponse<?>> filtering(PlayingFilteringDTO playingFilteringDTO);

    ResponseEntity<CustomAPIResponse<?>> recruitPlaying(Long playingId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> cancelPlaying(Long playingId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> scrapPlaying(Long playingId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> cancelScrap(Long playingId, MyUserDetailsService.MyUserDetails userDetails);


    ResponseEntity<CustomAPIResponse<?>> getMyPlayings(MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> deleteMyPlaying(Long playingId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> completePlaying(Long playingId, MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> recruitedPlayings(MyUserDetailsService.MyUserDetails userDetails);

    ResponseEntity<CustomAPIResponse<?>> writePlayingReview(PlayingReviewRequestDto playingReviewRequestDto, MyUserDetailsService.MyUserDetails userDetails);
}