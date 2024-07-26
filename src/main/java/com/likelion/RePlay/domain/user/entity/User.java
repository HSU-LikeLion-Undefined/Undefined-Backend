package com.likelion.RePlay.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.likelion.RePlay.domain.info.entity.InfoSubmit;
import com.likelion.RePlay.domain.learning.entity.*;
import com.likelion.RePlay.domain.playing.entity.*;
import com.likelion.RePlay.global.enums.District;
import com.likelion.RePlay.global.enums.State;
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
@Table(name="USERS")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private Long userId;

    @Column(name="PHONE_ID")
    private String phoneId; // 아이디

    @Column(name="PASSWORD")
    private String password;

    @Column(name="NICKNAME")
    private String nickname;

    @Enumerated(EnumType.STRING)
    private State state; // 시

    @Enumerated(EnumType.STRING)
    private District district; // 구

    @Column(name="YEAR")
    private Long year; // 출생연도

    @Column(name="PROFILE_IMAGE")
    private String profileImage; // 프로필 사진

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<UserRole> userRoles = new ArrayList<>();

    @Column(name = "INTRODUCE")
    private String introduce;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Playing> playings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PlayingReview> playingReviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PlayingComment> playingComments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PlayingScrap> playingScraps = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PlayingApply> playingApplies = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<InfoSubmit> infoSubmits = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Learning> learnings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningReview> learningReviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningComment> learningComments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningScrap> learningScraps = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningApply> learningApplies = new ArrayList<>();

    public void addRole(Role role) {
        UserRole userRole = new UserRole(this, role);
        this.userRoles.add(userRole);
    }

    public void changeIntroduce(String introduce) {
        this.introduce = introduce;
    }
}