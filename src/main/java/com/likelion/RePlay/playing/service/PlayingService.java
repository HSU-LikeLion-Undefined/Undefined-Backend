package com.likelion.RePlay.playing.service;

import com.likelion.RePlay.playing.dto.PlayingWriteRequestDTO;
import com.likelion.RePlay.util.response.CustomAPIResponse;
import org.springframework.http.ResponseEntity;

public interface PlayingService {
    ResponseEntity<CustomAPIResponse<?>> writePost(PlayingWriteRequestDTO postWriteRequestDTO);

    ResponseEntity<CustomAPIResponse<?>> getAllPosts();

    ResponseEntity<CustomAPIResponse<?>> getPost(Long playingId);
}