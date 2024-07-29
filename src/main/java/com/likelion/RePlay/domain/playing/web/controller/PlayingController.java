package com.likelion.RePlay.domain.playing.web.controller;

import com.likelion.RePlay.domain.playing.web.dto.PlayingCommentWriteRequestDTO;
import com.likelion.RePlay.domain.playing.web.dto.PlayingFilteringDTO;
import com.likelion.RePlay.domain.playing.web.dto.PlayingWriteRequestDTO;
import com.likelion.RePlay.domain.playing.service.PlayingServiceImpl;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/playing")
@RequiredArgsConstructor
public class PlayingController {

    private final PlayingServiceImpl playingService;

    @PostMapping("/writePost")
    private ResponseEntity<CustomAPIResponse<?>> writePost(@RequestBody PlayingWriteRequestDTO playingWriteRequestDTO, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.writePost(playingWriteRequestDTO, userDetails);
    }

    @PutMapping("/{playingId}")
    private ResponseEntity<CustomAPIResponse<?>> modifyPost(@PathVariable Long playingId, @RequestBody PlayingWriteRequestDTO playingWriteRequestDTO, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.modifyPost(playingId, playingWriteRequestDTO, userDetails);
    }

    @GetMapping("/getPlayings")
    private ResponseEntity<CustomAPIResponse<?>> getAllPosts() {
        return playingService.getAllPosts();
    }

    @GetMapping("/getPlayings/{playingId}")
    private ResponseEntity<CustomAPIResponse<?>> getPost(@PathVariable Long playingId) {
        return playingService.getPost(playingId);
    }

    @PostMapping("/filtering")
    private ResponseEntity<CustomAPIResponse<?>> filtering(@RequestBody PlayingFilteringDTO playingFilteringDTO) {
        return playingService.filtering(playingFilteringDTO);
    }

    @PostMapping("/{playingId}")
    private ResponseEntity<CustomAPIResponse<?>> recruitPlaying(@PathVariable Long playingId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.recruitPlaying(playingId, userDetails);
    }

    @DeleteMapping("/{playingId}")
    private ResponseEntity<CustomAPIResponse<?>> cancelPlaying(@PathVariable Long playingId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.cancelPlaying(playingId, userDetails);
    }

    @PostMapping("/{playingId}/scrap")
    private ResponseEntity<CustomAPIResponse<?>> scrapPlaying(@PathVariable Long playingId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.scrapPlaying(playingId, userDetails);
    }

    @DeleteMapping("/{playingId}/scrap")
    private ResponseEntity<CustomAPIResponse<?>> cancelScrap(@PathVariable Long playingId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.cancelScrap(playingId, userDetails);
    }

    @GetMapping("/myPlayings")
    private ResponseEntity<CustomAPIResponse<?>> getMyPlayings(@AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.getMyPlayings(userDetails);
    }

    @DeleteMapping("/myPlayings/{playingId}")
    private ResponseEntity<CustomAPIResponse<?>> deleteMyPlaying(@PathVariable Long playingId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.deleteMyPlaying(playingId, userDetails);
    }

    @PostMapping("/{playingId}/complete")
    private ResponseEntity<CustomAPIResponse<?>> completePlaying(@PathVariable Long playingId, @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.completePlaying(playingId, userDetails);
    }

    @GetMapping("/recruited")
    private ResponseEntity<CustomAPIResponse<?>> recruitedPlayings(@AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.recruitedPlayings(userDetails);
    }

    @GetMapping("/scrap")
    private ResponseEntity<CustomAPIResponse<?>> scrapPlayings(@AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.scrapPlayings(userDetails);
    }

    @PostMapping("/{playingId}/comment")
    private ResponseEntity<CustomAPIResponse<?>> commentPlaying(
            @PathVariable Long playingId,
            @RequestBody PlayingCommentWriteRequestDTO playingCommentWriteRequestDTO,
            @AuthenticationPrincipal MyUserDetailsService.MyUserDetails userDetails) {
        return playingService.commentPlaying(playingId, playingCommentWriteRequestDTO, userDetails);
    }

}