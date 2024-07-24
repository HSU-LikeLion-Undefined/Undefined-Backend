package com.likelion.RePlay.domain.playing.repository;

import com.likelion.RePlay.domain.playing.entity.Playing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PlayingRepository extends JpaRepository<Playing, Long>, QuerydslPredicateExecutor<Playing> {
}