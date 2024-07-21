package com.likelion.RePlay.entity.learning;

import com.likelion.RePlay.entity.User;
import com.likelion.RePlay.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="LEARNING_SCRAP")
public class LearningScrap  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LEARNING_SCARP_ID")
    private Long learningScrapId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "LEARNING_ID")
    private Learning learning;
}
