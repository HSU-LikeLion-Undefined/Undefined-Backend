package com.likelion.RePlay.domain.playing.service;

import com.likelion.RePlay.domain.playing.dto.PlayingFilteringDTO;
import com.likelion.RePlay.domain.playing.dto.PlayingWriteRequestDTO;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import org.springframework.http.ResponseEntity;


public interface PlayingService {
    ResponseEntity<CustomAPIResponse<?>> writePost(PlayingWriteRequestDTO postWriteRequestDTO);

    ResponseEntity<CustomAPIResponse<?>> getAllPosts();

    ResponseEntity<CustomAPIResponse<?>> getPost(Long playingId);

    ResponseEntity<CustomAPIResponse<?>> filtering(PlayingFilteringDTO playingFilteringDTO);

    ResponseEntity<CustomAPIResponse<?>> recruitPlaying(Long playingId, String phoneId);
}