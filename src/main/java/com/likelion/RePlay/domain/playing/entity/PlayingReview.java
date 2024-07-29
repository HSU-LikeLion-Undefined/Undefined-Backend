package com.likelion.RePlay.domain.playing.entity;

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
@Table(name="PLAYING_REVIEW")
public class PlayingReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PLAYING_REVIEW_ID")
    private Long playingReviewId;

    @Column(name="CONTENT")
    private String content;

    @Column(name="RATE")
    private Double rate;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PLAYING_ID")
    private Playing playing; // 놀이터 게시글 아이디
}
