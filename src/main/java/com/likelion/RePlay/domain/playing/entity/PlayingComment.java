package com.likelion.RePlay.domain.playing.entity;

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
@Table(name="PLAYING_COMMENT")
public class PlayingComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PLAYING_COMMENT_ID")
    private Long playingCommentId;

    @Column(name="CONTENT")
    private String content;

    @Column(name = "DATE")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PLAYING_ID")
    private Playing playing;

    // 자기 참조 관계 설정
    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private PlayingComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayingComment> replies = new ArrayList<>();
}
