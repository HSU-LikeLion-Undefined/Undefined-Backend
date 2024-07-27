package com.likelion.RePlay.domain.info.service;

import com.likelion.RePlay.domain.info.entity.Info;
import com.likelion.RePlay.domain.info.repository.InfoRepository;
import com.likelion.RePlay.domain.info.web.dto.GetAllInfoResponseDto;
import com.likelion.RePlay.domain.info.web.dto.GetOneInfoResponseDto;
import com.likelion.RePlay.domain.info.web.dto.InfoCreateDto;
import com.likelion.RePlay.domain.info.web.dto.InfoModifyDto;
import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.domain.user.repository.UserRepository;
import com.likelion.RePlay.global.enums.RoleName;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService.MyUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {
    private final InfoRepository infoRepository;
    private final UserRepository userRepository;
    private final InfoImageService infoImageService;

    @Transactional
    @Override
    public ResponseEntity<CustomAPIResponse<?>> createInfo(InfoCreateDto.InfoCreateRequest infoCreateRequest, List<MultipartFile> images, MyUserDetails userDetails) {
        Optional<User> isAdmin = userRepository.findByPhoneId(userDetails.getPhoneId());

        // 사용자가 존재하지 않거나 관리자가 아니라면 작성 권한 없음
        if (isAdmin.isEmpty() || isAdmin.get().getUserRoles().stream()
                .noneMatch(userRole -> userRole.getRole().getRoleName() == RoleName.ROLE_ADMIN)) {
            throw new RuntimeException("글 작성 권한이 없습니다.");
        }

        // writer를 userDetails에서 설정
        String writer = isAdmin.get().getNickname();
        String writerId = isAdmin.get().getPhoneId();

        Info info = infoCreateRequest.toEntity();
        info.setWriter(writer); // writer 설정
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
        // 수정할 글이 존재하지 않는다면 수정할 수 없음
        Optional<Info> optionalInfo = infoRepository.findById(infoModifyRequest.getInfoId());
        if (optionalInfo.isEmpty()) {
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.NOT_FOUND.value(), "해당 글을 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failResponse);
        }

        // 존재하지 않는 사용자거나 관리자가 아니라면 수정 권한 없음
        Optional<User> isAdmin = userRepository.findByPhoneId(userDetails.getPhoneId());
        if (isAdmin.isEmpty() || isAdmin.get().getUserRoles().stream()
                .noneMatch(userRole -> userRole.getRole().getRoleName() == RoleName.ROLE_ADMIN)) {
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.FORBIDDEN.value(), "글 수정 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(failResponse);
        }

        // 작성자 자체는 따로 수정하지 않도록,,,
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

        // CustomAPIResponse 객체 생성
        CustomAPIResponse<GetAllInfoResponseDto.FinalResponseDto> response = CustomAPIResponse.createSuccess(
                HttpStatus.OK.value(), responseDto, "전체 게시글 조회 성공");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getOneInfo(Long infoId) {
        // 게시글이 존재하지 않음
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
}