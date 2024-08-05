package com.likelion.RePlay.domain.info.repository;

import com.likelion.RePlay.domain.info.entity.Info;
import com.likelion.RePlay.domain.info.entity.InfoScrap;
import com.likelion.RePlay.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InfoScrapRepository extends JpaRepository<InfoScrap, Long> {
    Optional<InfoScrap> findByInfoScrapId(Long infoScrapId);
    Optional<InfoScrap> findByUserAndInfo(User user, Info info);
    List<InfoScrap> findByUser(User user);

}
