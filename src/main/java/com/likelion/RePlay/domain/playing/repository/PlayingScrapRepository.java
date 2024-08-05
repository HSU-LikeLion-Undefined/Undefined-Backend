package com.likelion.RePlay.domain.playing.repository;

import com.likelion.RePlay.domain.playing.entity.PlayingScrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayingScrapRepository extends JpaRepository<PlayingScrap, Long> {

    List<PlayingScrap> findAllByUserUserId(Long userId);
    Optional<PlayingScrap> findByUserPhoneId(String phoneId);
    Optional<PlayingScrap> findByUserPhoneIdAndPlayingPlayingId(String phoneId, Long playingId);

}
