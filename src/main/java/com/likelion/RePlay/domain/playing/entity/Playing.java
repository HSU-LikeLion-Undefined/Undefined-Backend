package com.likelion.RePlay.domain.playing.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.global.enums.*;
import com.likelion.RePlay.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Entity
@Builder
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="PLAYING")
public class Playing extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PLAYING_ID")
    private Long playingId;

    @Column(name="TITLE")
    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name="DATE")
    private Date date;

    @Enumerated(EnumType.STRING)
    private IsRecruit isRecruit;

    @Column(name = "TOTAL_COUNT")
    private Long totalCount;

    @Column(name="RECRUITMENT_COUNT")
    private Long recruitmentCount;

    @Column(name="CONTENT")
    private String content;

    @Column(name="LATITUDE")
    private double latitude;

    @Column(name="LONGITUDE")
    private double longitude;

    @Enumerated(EnumType.STRING)
    private IsCompleted isCompleted;

    @Column(name = "COST")
    private Long cost;

    @Column(name = "COST_DESCRIPTION")
    private String costDescription;

    @Enumerated(EnumType.STRING)
    private State state; // 시

    @Enumerated(EnumType.STRING)
    private District district; // 구

    @Column(name = "LOCATE")
    private String locate;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "playing", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PlayingReview> playingReviews;

    @OneToMany(mappedBy = "playing", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PlayingComment> playingComments;

    @OneToMany(mappedBy = "playing", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PlayingScrap> playingScraps;

    @OneToMany(mappedBy = "playing", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PlayingApply> playingApplies;


    // 연관관계 편의 메서드
    public void changeDate(Date date) {
        this.date = date;
    }

    public void changeLocate(String locate) {
        this.locate = locate;
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void changeTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public void changeCost(Long cost) {
        this.cost = cost;
    }

    public void changeCostDescription(String costDescription) {
        this.costDescription = costDescription;
    }

    public void changeIsRecruit(IsRecruit isRecruit) {
        this.isRecruit = isRecruit;
    }

    public void changeRecruitmentCount(Long recruitmentCount) {
        this.recruitmentCount = recruitmentCount;
    }

    public void changeIsCompleted(IsCompleted isCompleted) {
        this.isCompleted = isCompleted;
    }

}