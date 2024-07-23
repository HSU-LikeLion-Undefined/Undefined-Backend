package com.likelion.RePlay.playing.repository;

import com.likelion.RePlay.entity.playing.Playing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayingRepository extends JpaRepository<Playing, Long> {
}