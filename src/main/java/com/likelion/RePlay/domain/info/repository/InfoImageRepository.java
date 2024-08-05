package com.likelion.RePlay.domain.info.repository;

import com.likelion.RePlay.domain.info.entity.InfoImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoImageRepository extends JpaRepository<InfoImage, Long> {
}