package com.likelion.RePlay.domain.learning.repository;

import com.likelion.RePlay.domain.learning.entity.LearningMentor;
import com.likelion.RePlay.domain.learning.entity.LearningReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LearningReviewRepository extends JpaRepository<LearningReview, Long> {
    List<LearningReview> findByLearningMentor(LearningMentor learningMentor);
}
