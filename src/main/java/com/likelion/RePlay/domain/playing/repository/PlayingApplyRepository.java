package com.likelion.RePlay.domain.playing.repository;


import com.likelion.RePlay.domain.playing.entity.PlayingApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayingApplyRepository extends JpaRepository<PlayingApply, Long> {
    List<PlayingApply> findAllByUserUserId(Long userId);
    List<PlayingApply> findAllByPlayingPlayingId(Long playingId);
    Optional<PlayingApply> findByUserPhoneId(String phoneId);
    Optional<PlayingApply> findByUserPhoneIdAndPlayingPlayingId(String phoneId, Long playingId);
}
