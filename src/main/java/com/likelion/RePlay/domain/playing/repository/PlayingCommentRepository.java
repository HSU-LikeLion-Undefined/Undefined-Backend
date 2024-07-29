package com.likelion.RePlay.domain.playing.repository;

import com.likelion.RePlay.domain.playing.entity.Playing;
import com.likelion.RePlay.domain.playing.entity.PlayingComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayingCommentRepository extends JpaRepository<PlayingComment, Long> {
    Optional<PlayingComment> findByPlayingCommentId(Long playingCommentId);
    List<PlayingComment> findAllByPlayingPlayingId(Long playingId);
}