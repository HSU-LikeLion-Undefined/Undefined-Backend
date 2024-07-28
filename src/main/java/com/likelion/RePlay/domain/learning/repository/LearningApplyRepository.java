package com.likelion.RePlay.domain.learning.repository;

import com.likelion.RePlay.domain.learning.entity.LearningApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LearningApplyRepository extends JpaRepository<LearningApply, Long> {

    List<LearningApply> findAllByUserUserId(Long userId);
    Optional<LearningApply> findByUserPhoneId(String phoneId);
    Optional<LearningApply> findByUserPhoneIdAndLearningLearningId(String phoneId, Long learningId);

}
