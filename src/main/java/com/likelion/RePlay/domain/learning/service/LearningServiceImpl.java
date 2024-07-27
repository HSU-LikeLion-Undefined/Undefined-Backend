package com.likelion.RePlay.domain.learning.service;

import com.likelion.RePlay.domain.learning.entity.Learning;
import com.likelion.RePlay.domain.learning.entity.LearningApply;
import com.likelion.RePlay.domain.learning.entity.LearningScrap;
import com.likelion.RePlay.domain.learning.repository.LearningApplyRepository;
import com.likelion.RePlay.domain.learning.repository.LearningRepository;
import com.likelion.RePlay.domain.learning.repository.LearningScrapRepository;
import com.likelion.RePlay.domain.learning.web.dto.LearningApplyScrapRequestDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningFilteringDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningListDTO;
import com.likelion.RePlay.domain.learning.web.dto.LearningWriteRequestDTO;
import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.domain.user.repository.UserRepository;
import com.likelion.RePlay.global.enums.*;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class LearningServiceImpl implements LearningService{

    private final UserRepository userRepository;
    private final LearningRepository learningRepository;
    private final LearningApplyRepository learningApplyRepository;
    private final LearningScrapRepository learningScrapRepository;

    @Override
    public ResponseEntity<CustomAPIResponse<?>> writePost(LearningWriteRequestDTO learningWriteRequestDTO, MyUserDetailsService.MyUserDetails userDetails) {

        Optional<User> isAdmin = userRepository.findByPhoneId(userDetails.getPhoneId());

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

    @Override
    public ResponseEntity<CustomAPIResponse<?>> filtering(LearningFilteringDTO learningFilteringDTO) {

        List<Learning> allLearnings = learningRepository.findAll();

        List<Date> parseDates = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d", Locale.KOREA);
        try {
            for (String dateString : learningFilteringDTO.getDateList()) {
                Date date = formatter.parse(dateString);
                parseDates.add(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(404, "날짜 형식이 잘못되었습니다."));
        }

        List<Learning> filteredByDate = allLearnings;
        if (!parseDates.isEmpty()) {
            filteredByDate = filteredByDate.stream()
                    .filter(learning -> {
                        LocalDate learningDate = learning.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return parseDates.stream().anyMatch(date -> {
                            LocalDate filterDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            return learningDate.getMonth() == filterDate.getMonth() && learningDate.getDayOfMonth() == filterDate.getDayOfMonth();
                        });
                    })
                    .collect(Collectors.toList());
        }

        List<Learning> filteredByLocation = filteredByDate;
        if (learningFilteringDTO.getStateList() != null && !learningFilteringDTO.getStateList().isEmpty()) {
            filteredByLocation = filteredByLocation.stream()
                    .filter(learning -> {
                        boolean matches = false;
                        for (int i = 0; i < learningFilteringDTO.getStateList().size(); i++) {
                            State state = learningFilteringDTO.getStateList().get(i);
                            District district = (learningFilteringDTO.getDistrictList() != null && learningFilteringDTO.getDistrictList().size() > i)
                                    ? learningFilteringDTO.getDistrictList().get(i)
                                    : null;
                            if (learning.getState().equals(state) &&
                                    (district == null || district.equals(District.ALL) || learning.getDistrict().equals(district))) {
                                matches = true;
                                break;
                            }
                        }
                        return matches;
                    })
                    .collect(Collectors.toList());
        }

        List<Learning> filteredByCategory = filteredByLocation;
        if (learningFilteringDTO.getCategory() != null) {
            filteredByCategory = filteredByCategory.stream()
                    .filter(learning -> learning.getCategory().equals(learningFilteringDTO.getCategory()))
                    .collect(Collectors.toList());
        }

        List<LearningListDTO.LearningResponse> learningResponse = new ArrayList<>();
        for (Learning result : filteredByCategory) {
            LearningListDTO.LearningResponse response = LearningListDTO.LearningResponse.builder()
                    .category(result.getCategory())
                    .title(result.getTitle())
                    .state(result.getState())
                    .district(result.getDistrict())
                    .date(result.getDate())
                    .totalCount(result.getTotalCount())
                    .recruitmentCount(result.getRecruitmentCount())
                    .imageUrl(result.getImageUrl())
                    .build();
            learningResponse.add(response);
        }

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, learningResponse, "조건에 맞는 게시글들을 성공적으로 불러왔습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> recruitLearning(Long learningId, LearningApplyScrapRequestDTO learningApplyScrapRequestDTO) {

        String phoneId = learningApplyScrapRequestDTO.getPhoneId();

        Optional<Learning> findLearning = learningRepository.findById(learningId);
        Optional<User> findUser = userRepository.findByPhoneId(phoneId);

        if (findLearning.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        } else if (findUser.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 유저입니다."));
        }

        if (findLearning.get().getUser() == findUser.get()) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "내가 올린 게시글에 참가할 수 없습니다."));
        }

        if (findLearning.get().getIsRecruit() ==IsRecruit.FALSE) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "인원이 마감되었습니다."));
        } else if (findLearning.get().getIsCompleted() == IsCompleted.TRUE) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "모집 완료된 활동입니다."));
        }

        Optional<LearningApply> findLearningApply = learningApplyRepository.findByUserPhoneId(phoneId);

        if (findLearningApply.isEmpty()) {
            LearningApply newApply = LearningApply.builder()
                    .learning(findLearning.get())
                    .user(findUser.get())
                    .build();

            learningApplyRepository.save(newApply);
        } else {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "이미 신청한 활동입니다."));
        }

        findLearning.get().changeRecruitmentCount(findLearning.get().getRecruitmentCount() + 1);

        if (findLearning.get().getRecruitmentCount() == findLearning.get().getTotalCount()) {
            findLearning.get().changeIsRecruit(IsRecruit.FALSE);
        }

        LearningListDTO.LearningResponse learningResponse = LearningListDTO.LearningResponse.builder()
                .category(findLearning.get().getCategory())
                .title(findLearning.get().getTitle())
                .date(findLearning.get().getDate())
                .locate(findLearning.get().getLocate())
                .imageUrl(findLearning.get().getImageUrl())
                .build();

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, learningResponse, "활동 신청이 완료되었습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> cancelLearning(Long learningId, LearningApplyScrapRequestDTO learningApplyScrapRequestDTO) {

        String phoneId = learningApplyScrapRequestDTO.getPhoneId();

        // 놀이터 게시글이 DB에 존재하는가?
        Optional<Learning> findLearning = learningRepository.findById(learningId);
        // 존재하는 사용자인가?
        Optional<User> findUser = userRepository.findByPhoneId(phoneId);
        // 존재하는 신청정보인가?
        Optional<LearningApply> findApply = learningApplyRepository.findByUserPhoneId(phoneId);

        // 존재하지 않는다면 오류 반환
        if (findLearning.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        } else if (findUser.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 유저입니다."));
        } else if (findApply.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 신청 정보입니다."));
        }

        // 게시글 작성자와 현재 작성자가 같다면 참가 취소 불가
        if (findLearning.get().getUser() == findUser.get()) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "내가 올린 게시글에 참가 취소할 수 없습니다."));
        }

        // 해당 참가자의 참가를 취소하고, 해당 게시글의 인원을 한 명 줄인다.
        // 만약 정원이 다 찬 경우에서 취소한다면, 모집 완료를 모집 중으로 바꾼다.
        if (findLearning.get().getIsRecruit() == IsRecruit.FALSE) {
            findLearning.get().changeIsRecruit(IsRecruit.TRUE);
        }
        findLearning.get().changeRecruitmentCount(findLearning.get().getRecruitmentCount() - 1);

        learningApplyRepository.delete(findApply.get());

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, null, "활동 신청이 취소되었습니다."));

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> scrapLearning(Long learningId, LearningApplyScrapRequestDTO learningApplyScrapRequestDTO) {

        String phoneId = learningApplyScrapRequestDTO.getPhoneId();

        Optional<Learning> findLearning = learningRepository.findById(learningId);
        Optional<User> findUser = userRepository.findByPhoneId(phoneId);

        if (findLearning.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        } else if (findUser.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 유저입니다."));
        }

        Optional<LearningScrap> findLearningScrap = learningScrapRepository.findByUserPhoneId(phoneId);

        if (findLearningScrap.isEmpty()) {
            LearningScrap newScrap = LearningScrap.builder()
                    .learning(findLearning.get())
                    .user(findUser.get())
                    .build();

            learningScrapRepository.save(newScrap);
            return ResponseEntity.status(200)
                    .body(CustomAPIResponse.createSuccess(200, null, "스크랩되었습니다."));
        }else {

            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400,  "이미 스크랩했습니다."));
        }
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> cancelScrap(Long learningId, LearningApplyScrapRequestDTO learningApplyScrapRequestDTO) {

        String phoneId = learningApplyScrapRequestDTO.getPhoneId();

        Optional<Learning> findLearning = learningRepository.findById(learningId);
        Optional<User> findUser = userRepository.findByPhoneId(phoneId);

        if (findLearning.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        } else if (findUser.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 유저입니다."));
        }

        Optional<LearningScrap> findLearningScrap = learningScrapRepository.findByUserPhoneId(phoneId);

        if (findLearningScrap.isPresent()) {
            learningScrapRepository.delete(findLearningScrap.get());
            return ResponseEntity.status(200)
                    .body(CustomAPIResponse.createSuccess(200, null, "스크랩이 취소되었습니다."));
        }else {

            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400,  "스크랩하지 않은 게시글입니다."));
        }
    }


}
