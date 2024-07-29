package com.likelion.RePlay.domain.learning.repository;

import com.likelion.RePlay.domain.learning.entity.LearningComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LearningCommentRepository extends JpaRepository<LearningComment, Long> {
    Optional<LearningComment> findByLearningCommentId(Long learningCommentId);
    List<LearningComment> findAllByLearningLearningId(Long learningId);
}