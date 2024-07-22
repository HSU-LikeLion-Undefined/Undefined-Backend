package com.likelion.RePlay.domain.playing.entity;

import com.likelion.RePlay.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="PLAYING_IMAGE")
public class PlayingImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PLAYING_IMAGE_ID")
    private Long playingImageId;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "PLAYING_ID")
    private Playing playing;
}
