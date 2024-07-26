package com.likelion.RePlay.domain.info.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.likelion.RePlay.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="INFO_IMAGE")
public class InfoImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IMAGE_ID")
    private Long imageId;

    @Column(name="IMAGE_URL")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name="INFO_ID")
    // 양방향 관계에서 순환 참조 문제 해결
    @JsonBackReference
    private Info info;

    public void setInfo(Info info) {
        this.info = info;
    }

}