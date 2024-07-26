package com.likelion.RePlay.domain.info.service;

import com.likelion.RePlay.domain.info.entity.Info;
import com.likelion.RePlay.domain.info.repository.InfoRepository;
import com.likelion.RePlay.domain.info.web.dto.InfoCreateDto;
import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.domain.user.repository.UserRepository;
import com.likelion.RePlay.global.enums.RoleName;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {
    private final InfoRepository infoRepository;
    private final UserRepository userRepository;
    private final InfoImageService infoImageService;

    @Transactional
    @Override
    public ResponseEntity<CustomAPIResponse<?>> createInfo(InfoCreateDto.InfoCreateRequest infoCreateRequest, List<MultipartFile> images) {
        Optional<User> isAdmin = userRepository.findByPhoneId(infoCreateRequest.getWriter());

        if (isAdmin.isEmpty() || isAdmin.get().getUserRoles().stream()
                .noneMatch(userRole -> userRole.getRole().getRoleName() == RoleName.ROLE_ADMIN)) {
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.FORBIDDEN.value(), "글 작성 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(failResponse);
        }

        Info info = infoCreateRequest.toEntity();
        infoRepository.save(info);

        if (images != null && !images.isEmpty()) {
            infoImageService.uploadImages(info, images);
        }

        InfoCreateDto.CreateInfo createInfo = InfoCreateDto.CreateInfo.builder()
                .infoId(info.getInfoId())
                .updatedAt(info.getUpdatedAt())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CustomAPIResponse.createSuccess(HttpStatus.CREATED.value(), createInfo, "생생정보터에 글이 성공적으로 업로드되었습니다."));
    }
}