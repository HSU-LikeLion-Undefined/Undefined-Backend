package com.likelion.RePlay.domain.info.repository;


import com.likelion.RePlay.domain.info.entity.Info;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InfoRepository extends JpaRepository<Info, Long> {
    Optional<Info> findByInfoId(Long infoId);
}
