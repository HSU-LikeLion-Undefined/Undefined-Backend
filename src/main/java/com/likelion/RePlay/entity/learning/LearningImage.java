package com.likelion.RePlay.entity.learning;

import com.likelion.RePlay.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="LEARNING_IMAGE")
public class LearningImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LEARNING_IMAGE_ID")
    private Long LearningImageId;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "LEARNING_ID")
    private Learning learning;
}
