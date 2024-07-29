package com.likelion.RePlay.domain.learning.repository;

import com.likelion.RePlay.domain.learning.entity.LearningReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningReviewRepository extends JpaRepository<LearningReview, Long> {
}
