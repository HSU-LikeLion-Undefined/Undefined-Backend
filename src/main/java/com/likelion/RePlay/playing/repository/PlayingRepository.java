package com.likelion.RePlay.playing.repository;

import com.likelion.RePlay.entity.playing.Playing;
import com.likelion.RePlay.enums.Category;
import com.likelion.RePlay.enums.District;
import com.likelion.RePlay.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface PlayingRepository extends JpaRepository<Playing, Long>, QuerydslPredicateExecutor<Playing> {
    Optional<Playing> findPlayingsByCategoryAndStateAndDistrict(Category category, State state, District district);
}