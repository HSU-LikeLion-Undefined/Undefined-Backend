package com.likelion.RePlay.domain.learning.repository;

import com.likelion.RePlay.domain.learning.entity.Learning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningRepository extends JpaRepository<Learning, Long> {
}
