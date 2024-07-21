package com.likelion.RePlay.entity.playing;

import com.likelion.RePlay.entity.User;
import com.likelion.RePlay.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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
    private Long content;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "PLAYING_ID")
    private Playing playing;
}
