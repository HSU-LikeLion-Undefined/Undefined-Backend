package com.likelion.RePlay.domain.info.service;

import com.likelion.RePlay.domain.info.entity.Info;
import com.likelion.RePlay.domain.info.repository.InfoRepository;
import com.likelion.RePlay.domain.info.web.dto.*;
import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.domain.user.repository.UserRepository;
import com.likelion.RePlay.global.enums.RoleName;
import com.likelion.RePlay.global.mail.MailService;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService.MyUserDetails;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {
    private final InfoRepository infoRepository;
    private final UserRepository userRepository;
    private final InfoImageService infoImageService;

    @Autowired
    private MailService mailService;

    @Transactional
    @Override
    public ResponseEntity<CustomAPIResponse<?>> createInfo(InfoCreateDto.InfoCreateRequest infoCreateRequest, List<MultipartFile> images, MyUserDetails userDetails) {
        Optional<User> isAdmin = userRepository.findByPhoneId(userDetails.getPhoneId());

        if (isAdmin.isEmpty() || isAdmin.get().getUserRoles().stream()
                .noneMatch(userRole -> userRole.getRole().getRoleName() == RoleName.ROLE_ADMIN)) {
            throw new RuntimeException("글 작성 권한이 없습니다.");
        }

        String writer = isAdmin.get().getNickname();
        String writerId = isAdmin.get().getPhoneId();

        Info info = infoCreateRequest.toEntity();
        info.setWriter(writer);
        info.setWriterId(writerId);
        infoRepository.save(info);

        if (images != null && !images.isEmpty()) {
            infoImageService.uploadImages(info, images);
        }

        InfoCreateDto.CreateInfo createInfoResponse = InfoCreateDto.CreateInfo.builder()
                .infoId(info.getInfoId())
                .updatedAt(info.getUpdatedAt())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomAPIResponse.createSuccess(HttpStatus.OK.value(), createInfoResponse, "생생정보글이 성공적으로 작성되었습니다."));
    }

    @Transactional
    @Override
    public ResponseEntity<CustomAPIResponse<?>> modifyInfo(InfoModifyDto.InfoModifyRequest infoModifyRequest, List<MultipartFile> images, MyUserDetails userDetails) {
        Optional<Info> optionalInfo = infoRepository.findById(infoModifyRequest.getInfoId());
        if (optionalInfo.isEmpty()) {
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.NOT_FOUND.value(), "해당 글을 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failResponse);
        }

        Optional<User> isAdmin = userRepository.findByPhoneId(userDetails.getPhoneId());
        if (isAdmin.isEmpty() || isAdmin.get().getUserRoles().stream()
                .noneMatch(userRole -> userRole.getRole().getRoleName() == RoleName.ROLE_ADMIN)) {
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.FORBIDDEN.value(), "글 수정 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(failResponse);
        }

        Info info = optionalInfo.get();
        info.setTitle(infoModifyRequest.getTitle());
        info.setContent(infoModifyRequest.getContent());
        info.setInfoNum(infoModifyRequest.getInfoNum());

        infoRepository.save(info);

        if (images != null && !images.isEmpty()) {
            infoImageService.uploadImages(info, images);
        }

        InfoModifyDto.ModifyInfo modifyInfoResponse = InfoModifyDto.ModifyInfo.builder()
                .updatedAt(info.getUpdatedAt())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomAPIResponse.createSuccess(HttpStatus.OK.value(), modifyInfoResponse, "생생정보글이 성공적으로 수정되었습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<GetAllInfoResponseDto.FinalResponseDto>> getAllInfo() {
        List<Info> allInfos = infoRepository.findAll();

        List<GetAllInfoResponseDto.GetInfo> allInfoDtos = allInfos.stream()
                .map(info -> GetAllInfoResponseDto.GetInfo.builder()
                        .infoId(info.getInfoId())
                        .title(info.getTitle())
                        .content(info.getContent())
                        .writer(info.getWriter())
                        .infoNum(info.getInfoNum())
                        .thumbnailUrl(info.getImages().isEmpty() ? null : info.getImages().get(0).getImageUrl())
                        .createdAt(info.getCreatedAt().toLocalDate())
                        .build())
                .collect(Collectors.toList());

        GetAllInfoResponseDto.FinalResponseDto responseDto = GetAllInfoResponseDto.FinalResponseDto.builder()
                .allInfos(allInfoDtos)
                .build();

        CustomAPIResponse<GetAllInfoResponseDto.FinalResponseDto> response = CustomAPIResponse.createSuccess(
                HttpStatus.OK.value(), responseDto, "전체 게시글 조회 성공");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getOneInfo(Long infoId) {
        Optional<Info> optionalInfo = infoRepository.findById(infoId);
        if (optionalInfo.isEmpty()) {
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.NOT_FOUND.value(), "해당 글을 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failResponse);
        }

        Info info = optionalInfo.get();
        GetOneInfoResponseDto infoResponseDto = GetOneInfoResponseDto.toEntity(info);

        CustomAPIResponse<GetOneInfoResponseDto> successResponse = CustomAPIResponse
                .createSuccess(HttpStatus.OK.value(), infoResponseDto, "게시글 조회 성공");

        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> submitInfo(
            InfoSubmitRequestDto.InfoSubmitRequest infoSubmitRequest,
            List<MultipartFile> images, MyUserDetails userDetails) {
        try {
            String writer = userDetails.getUsername();

            InfoSubmitResponseDto responseDto = InfoSubmitResponseDto.builder()
                    .title(infoSubmitRequest.getTitle())
                    .content(infoSubmitRequest.getContent())
                    .writer(writer)
                    .build();

            List<MultipartFile> attachments = images != null ? images : Collections.emptyList();

            mailService.sendMail(responseDto, attachments);

            CustomAPIResponse<Object> successResponse =
                    CustomAPIResponse.createSuccess(HttpStatus.OK.value(), null, "투고 신청이 완료되었습니다.");
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "정보 제출 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(failResponse);
        } catch (Exception e) {
            e.printStackTrace();
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "알 수 없는 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(failResponse);
        }
    }
}