package com.likelion.RePlay.domain.info.service;

import com.likelion.RePlay.domain.info.entity.Info;
import com.likelion.RePlay.domain.info.entity.InfoImage;
import com.likelion.RePlay.domain.info.repository.InfoImageRepository;
import com.likelion.RePlay.global.amazon.S3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InfoImageServiceImpl implements InfoImageService {
    private final InfoImageRepository infoImageRepository;
    private final S3Service s3Service;

    @Transactional
    @Override
    public void uploadImages(Info info, List<MultipartFile> images) {
        List<InfoImage> infoImages = images.stream().map(file -> {
            try {
                String imageUrl = s3Service.uploadFile(file);
                return InfoImage.builder()
                        .imageUrl(imageUrl)
                        .info(info)
                        .build();
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드에 실패하였습니다.", e);
            }
        }).collect(Collectors.toList());

        infoImages.forEach(infoImageRepository::save);
    }
}