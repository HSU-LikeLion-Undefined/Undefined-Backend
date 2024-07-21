package com.likelion.RePlay.entity.playing;

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
@Table(name="PLAYING_REVIEW")
public class PlayingReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PLAYING_REVIEW_ID")
    private Long playingReviewId;

    @Column(name="CONTENT")
    private Long content;

    @Column(name="RATE")
    private BigDecimal rate;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PLAYING_ID")
    private Playing playing; // 참여회원
}
