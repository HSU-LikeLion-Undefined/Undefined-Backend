package com.likelion.RePlay.playing.repository;

import com.likelion.RePlay.entity.playing.PlayingApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayingApplyRepository extends JpaRepository<PlayingApply, Long> {
    Optional<PlayingApply> findByUserPhoneId(String phoneId);
}
