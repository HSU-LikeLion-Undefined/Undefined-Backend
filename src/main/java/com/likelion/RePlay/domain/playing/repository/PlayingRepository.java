package com.likelion.RePlay.domain.playing.repository;

import com.likelion.RePlay.domain.playing.entity.Playing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayingRepository extends JpaRepository<Playing, Long> {
    List<Playing> findAllByUserPhoneId(String phoneId);
}