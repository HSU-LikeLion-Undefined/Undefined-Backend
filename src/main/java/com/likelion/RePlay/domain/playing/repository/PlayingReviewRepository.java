package com.likelion.RePlay.domain.playing.repository;

import com.likelion.RePlay.domain.playing.entity.PlayingReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayingReviewRepository extends JpaRepository<PlayingReview, Long> {
}
