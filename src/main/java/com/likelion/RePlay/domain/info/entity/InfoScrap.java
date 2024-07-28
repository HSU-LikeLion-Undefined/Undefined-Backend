package com.likelion.RePlay.domain.info.entity;

import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor(access=AccessLevel.PROTECTED)
@Table(name="INFO_SCRAP")
public class InfoScrap extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="INFO_SCRAP_ID")
    private Long infoScrapId;

    @ManyToOne
    @JoinColumn(name = "INFO_ID")
    private Info info;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

}