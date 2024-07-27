package com.likelion.RePlay.domain.playing.service;

import com.likelion.RePlay.domain.playing.web.dto.PlayingApplyScrapRequestDTO;
import com.likelion.RePlay.domain.playing.web.dto.PlayingFilteringDTO;
import com.likelion.RePlay.domain.playing.web.dto.PlayingWriteRequestDTO;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import org.springframework.http.ResponseEntity;


public interface PlayingService {
    ResponseEntity<CustomAPIResponse<?>> writePost(PlayingWriteRequestDTO postWriteRequestDTO);

    ResponseEntity<CustomAPIResponse<?>> getAllPosts();

    ResponseEntity<CustomAPIResponse<?>> getPost(Long playingId);

    ResponseEntity<CustomAPIResponse<?>> filtering(PlayingFilteringDTO playingFilteringDTO);

    ResponseEntity<CustomAPIResponse<?>> recruitPlaying(Long playingId, PlayingApplyScrapRequestDTO playingApplyScrapRequestDTO);

    ResponseEntity<CustomAPIResponse<?>> cancelPlaying(Long playingId, PlayingApplyScrapRequestDTO playingApplyScrapRequestDTO);

    ResponseEntity<CustomAPIResponse<?>> scrapPlaying(Long playingId, PlayingApplyScrapRequestDTO playingApplyScrapRequestDTO);

    ResponseEntity<CustomAPIResponse<?>> cancelScrap(Long playingId, PlayingApplyScrapRequestDTO playingApplyScrapRequestDTO);
}