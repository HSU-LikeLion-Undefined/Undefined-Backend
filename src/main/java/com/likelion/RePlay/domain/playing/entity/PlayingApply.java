package com.likelion.RePlay.domain.playing.entity;

import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="PLAYING_APPLY")
public class PlayingApply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PLAYING_APPLY_ID")
    private Long playingApplyId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PLAYING_ID")
    private Playing playing;

    // 연관관계 편의 메서드
    public void changeUser(User user) {
        this.user = user;
    }

    public void changePlaying(Playing playing) {
        this.playing = playing;
    }
}
