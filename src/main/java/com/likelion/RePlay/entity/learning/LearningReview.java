package com.likelion.RePlay.entity.learning;

import com.likelion.RePlay.entity.User;
import com.likelion.RePlay.util.entity.BaseEntity;
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
    private Long LearningReviewId;

    @Column(name="CONTENT")
    private Long content;

    @Column(name="RATE")
    private BigDecimal rate;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "LEARNING_ID")
    private Learning learning; // 참여회원
}
