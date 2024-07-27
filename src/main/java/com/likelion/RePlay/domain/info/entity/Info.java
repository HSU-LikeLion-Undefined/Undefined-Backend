package com.likelion.RePlay.domain.info.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.likelion.RePlay.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="INFO")
public class Info extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="INFO_ID")
    private Long infoId;

    @Setter
    @Column(name = "TITLE")
    private String title;

    @Setter
    @Column(name = "CONTENT")
    private String content;

    @Column(name="WRITER")
    private String writer; //작성자

    @Setter
    @Column(name="INFO_NUM")
    private Long infoNum; //몇 호인지

    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<InfoImage> images=new ArrayList<>();

    public void addImage(InfoImage infoImage) {
        images.add(infoImage);
        infoImage.setInfo(this);
    }

    public void deleteImage(InfoImage infoImage) {
        images.remove(infoImage);
        infoImage.setInfo(null);
    }


}
