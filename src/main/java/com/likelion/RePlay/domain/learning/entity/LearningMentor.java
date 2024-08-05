package com.likelion.RePlay.domain.learning.entity;

import com.likelion.RePlay.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="LEARNING_MENTOR")
public class LearningMentor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LEARNING_MENTOR_ID")
    private Long learningMentorId;

    @Column
    private String mentorName;

    @Column
    private String introduce;

    // 연관관계 편의 메서드
    public void changeIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
