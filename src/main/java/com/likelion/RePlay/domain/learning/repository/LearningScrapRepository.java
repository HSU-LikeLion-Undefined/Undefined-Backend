package com.likelion.RePlay.domain.learning.repository;

import com.likelion.RePlay.domain.learning.entity.LearningApply;
import com.likelion.RePlay.domain.learning.entity.LearningScrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LearningScrapRepository extends JpaRepository<LearningScrap, Long> {
    Optional<LearningScrap> findByUserPhoneId(String phoneId);
    Optional<LearningScrap> findByUserPhoneIdAndLearningLearningId(String phoneId, Long learningId);
}
