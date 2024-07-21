package com.likelion.RePlay.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.likelion.RePlay.entity.info.InfoSubmit;
import com.likelion.RePlay.entity.learning.*;
import com.likelion.RePlay.entity.playing.*;
import com.likelion.RePlay.enums.District;
import com.likelion.RePlay.enums.State;
import com.likelion.RePlay.enums.Role;
import com.likelion.RePlay.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="USER")
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

    @Enumerated(EnumType.STRING)
    private Role role; // 관리자인지, 회원인지 구분하기 위함


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Playing> playings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PlayingReview> playingReviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PlayingComment> playingComments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PlayingScrap> playingScraps;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PlayingApply> playingApplies;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<InfoSubmit> infoSubmits;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Learning> learnings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningReview> learningReviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningComment> learningComments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningScrap> learningScraps;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LearningApply> learningApplies;

}
