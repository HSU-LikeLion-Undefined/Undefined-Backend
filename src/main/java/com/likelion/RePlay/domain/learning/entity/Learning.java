package com.likelion.RePlay.domain.learning.entity;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="LEARNING")
public class Learning extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LEARNING_ID")
    private Long learningId;

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

    @Column(name = "LOCATE")
    private String locate;

    @Enumerated(EnumType.STRING)
    private State state;

    @Enumerated(EnumType.STRING)
    private District district;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "MENTOR_ID")
    private LearningMentor learningMentor;

    @OneToMany(mappedBy = "learning", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningReview> learningReviews;

    @OneToMany(mappedBy = "learning", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningComment> learningComments;

    @OneToMany(mappedBy = "learning", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningScrap> learningScraps;

    @OneToMany(mappedBy = "learning", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningApply> learningApplies;

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

    public void changeIsRecruit(IsRecruit isRecruit) {
        this.isRecruit = isRecruit;
    }

    public void changeRecruitmentCount(Long recruitmentCount) {
        this.recruitmentCount = recruitmentCount;
    }
    public void changeLearningMentor(LearningMentor learningMentor) {
        this.learningMentor = learningMentor;
    }
}