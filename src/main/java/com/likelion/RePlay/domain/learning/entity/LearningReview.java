package com.likelion.RePlay.domain.learning.entity;

import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="LEARNING_REVIEW")
public class LearningReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LEARNING_REVIEW_ID")
    private Long learningReviewId;

    @Column(name="CONTENT")
    private String content;

    @Column(name="RATE")
    private BigDecimal rate;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "LEARNING_ID")
    private Learning learning;

    @ManyToOne
    @JoinColumn(name = "MENTOR_ID")
    private LearningMentor learningMentor;
}
