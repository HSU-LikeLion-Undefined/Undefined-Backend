package com.likelion.RePlay.domain.playing.repository;


import com.likelion.RePlay.domain.playing.entity.PlayingApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayingApplyRepository extends JpaRepository<PlayingApply, Long> {
    Optional<PlayingApply> findByUserPhoneId(String phoneId);
}
