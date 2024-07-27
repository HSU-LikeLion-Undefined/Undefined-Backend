package com.likelion.RePlay.domain.learning.service;

import com.likelion.RePlay.domain.learning.entity.Learning;
import com.likelion.RePlay.domain.learning.repository.LearningApplyRepository;
import com.likelion.RePlay.domain.learning.repository.LearningRepository;
import com.likelion.RePlay.domain.learning.web.dto.LearningListDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningWriteRequestDTO;
import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.domain.user.repository.UserRepository;
import com.likelion.RePlay.global.enums.IsCompleted;
import com.likelion.RePlay.global.enums.IsRecruit;
import com.likelion.RePlay.global.enums.RoleName;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class LearningServiceImpl implements LearningService{

    private final UserRepository userRepository;
    private final LearningRepository learningRepository;
    private final LearningApplyRepository learningApplyRepository;

    @Override
    public ResponseEntity<CustomAPIResponse<?>> writePost(LearningWriteRequestDTO learningWriteRequestDTO) {

        Optional<User> isAdmin = userRepository.findByPhoneId(learningWriteRequestDTO.getPhoneId());

        // 관리자만 글을 작성할 수 있다.
        if (isAdmin.isEmpty() || isAdmin.get().getUserRoles().stream()
                .noneMatch(userRole -> userRole.getRole().getRoleName() == RoleName.ROLE_ADMIN)) {

            return ResponseEntity.status(403)
                    .body(CustomAPIResponse.createFailWithout(403, "글 작성 권한이 없습니다."));
        }

        String dateStr = learningWriteRequestDTO.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 M월 d일 a h시 m분");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Learning newLearning = Learning.builder()
                .user(isAdmin.get())
                .title(learningWriteRequestDTO.getTitle())
                .category(learningWriteRequestDTO.getCategory())
                .date(date)
                .isRecruit(IsRecruit.TRUE)
                .isCompleted(IsCompleted.FALSE)
                .totalCount(learningWriteRequestDTO.getTotalCount())
                .recruitmentCount(0L)
                .content(learningWriteRequestDTO.getContent())
                .locate(learningWriteRequestDTO.getLocate())
                .latitude(learningWriteRequestDTO.getLatitude())
                .longitude(learningWriteRequestDTO.getLongitude())
                .state(learningWriteRequestDTO.getState())
                .district(learningWriteRequestDTO.getDistrict())
                .imageUrl(learningWriteRequestDTO.getImageUrl())
                .build();

        learningRepository.save(newLearning);

        return ResponseEntity.status(201)
                .body(CustomAPIResponse.createSuccess(201, null, "게시글을 성공적으로 작성하였습니다."));

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getAllPosts() {

        List<Learning> learnings = learningRepository.findAll();

        List<LearningListDTO.LearningResponse> learningResponses = new ArrayList<>();

        for (Learning learning : learnings) {
            learningResponses.add(LearningListDTO.LearningResponse.builder()
                    .category(learning.getCategory())
                    .title(learning.getTitle())
                    .state(learning.getState())
                    .district(learning.getDistrict())
                    .date(learning.getDate())
                    .totalCount(learning.getTotalCount())
                    .recruitmentCount(learning.getRecruitmentCount())
                    .imageUrl(learning.getImageUrl())
                    .build());
        }

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, learningResponses, "게시글 목록을 성공적으로 불러왔습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getPost(Long learningId) {

        Optional<Learning> findLearning = learningRepository.findById(learningId);

        if (findLearning.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        User user = findLearning.get().getUser();

        LearningListDTO.LearningResponse  learningResponse = LearningListDTO.LearningResponse.builder()
                .nickname(user.getNickname())
                .introduce(user.getIntroduce())
                .category(findLearning.get().getCategory())
                .title(findLearning.get().getTitle())
                .date(findLearning.get().getDate())
                .locate(findLearning.get().getLocate())
                .state(findLearning.get().getState())
                .district(findLearning.get().getDistrict())
                .totalCount(findLearning.get().getTotalCount())
                .recruitmentCount(findLearning.get().getRecruitmentCount())
                .content(findLearning.get().getContent())
                .imageUrl(findLearning.get().getImageUrl())
                .build();

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, learningResponse, "특정 게시글을 성공적으로 불러왔습니다."));
    }


}
