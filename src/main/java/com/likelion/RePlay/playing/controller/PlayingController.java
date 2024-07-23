package com.likelion.RePlay.playing.controller;

import com.likelion.RePlay.playing.dto.PlayingWriteRequestDTO;
import com.likelion.RePlay.playing.service.PlayingServiceImpl;
import com.likelion.RePlay.util.response.CustomAPIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/playing")
@RequiredArgsConstructor
public class PlayingController {

    private final PlayingServiceImpl playingService;

    @PostMapping("/writePost")
    private ResponseEntity<CustomAPIResponse<?>> createPost(@RequestBody PlayingWriteRequestDTO playingWriteRequestDTO) {
        return playingService.writePost(playingWriteRequestDTO);
    }

}