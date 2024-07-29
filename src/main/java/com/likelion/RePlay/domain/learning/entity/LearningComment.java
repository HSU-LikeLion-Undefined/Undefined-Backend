package com.likelion.RePlay.domain.learning.entity;

import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="LEARNING_COMMENT")
public class LearningComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Learning_COMMENT_ID")
    private Long LearningCommentId;

    @Column(name="CONTENT")
    private String content;

    @Column(name = "DATE")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "LEARNING_ID")
    private Learning learning;

    // 자기 참조 관계 설정
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private LearningComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearningComment> replies = new ArrayList<>();
}
