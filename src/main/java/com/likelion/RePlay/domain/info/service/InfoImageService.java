package com.likelion.RePlay.domain.info.service;

import com.likelion.RePlay.domain.info.entity.Info;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InfoImageService {
        void uploadImages(Info info, List<MultipartFile> images);
}