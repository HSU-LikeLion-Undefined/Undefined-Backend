package com.likelion.RePlay.domain.playing.repository;

import com.likelion.RePlay.domain.playing.entity.PlayingReview;
import com.likelion.RePlay.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayingReviewRepository extends JpaRepository<PlayingReview, Long> {

    @Query("SELECT pr FROM PlayingReview pr JOIN pr.playing p WHERE p.user.nickname = :nickname")
    List<PlayingReview> findReviewsByRecipientNickname(@Param("nickname") String nickname);
}
