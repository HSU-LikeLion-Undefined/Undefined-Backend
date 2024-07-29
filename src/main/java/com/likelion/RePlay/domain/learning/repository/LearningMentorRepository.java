package com.likelion.RePlay.domain.learning.repository;

import com.likelion.RePlay.domain.learning.entity.LearningMentor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LearningMentorRepository extends JpaRepository<LearningMentor, Long> {
    Optional<LearningMentor> findByMentorName(String learningMentorName);

}
