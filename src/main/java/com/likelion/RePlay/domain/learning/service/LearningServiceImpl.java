package com.likelion.RePlay.domain.learning.service;

import com.likelion.RePlay.domain.learning.entity.*;
import com.likelion.RePlay.domain.learning.repository.*;
import com.likelion.RePlay.domain.learning.web.dto.*;
import com.likelion.RePlay.domain.playing.web.dto.ApplicantListDTO;
import com.likelion.RePlay.domain.playing.web.dto.CommentListDTO;
import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.domain.user.repository.UserRepository;
import com.likelion.RePlay.global.amazon.S3.S3Service;
import com.likelion.RePlay.global.enums.*;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.likelion.RePlay.global.security.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final LearningMentorRepository learningMentorRepository;
    private final LearningReviewRepository learningReviewRepository;
    private final LearningCommentRepository learningCommentRepository;
    private final S3Service s3Service;

    @Override
    public ResponseEntity<CustomAPIResponse<?>> writePost(LearningWriteRequestDTO learningWriteRequestDTO, MultipartFile learningImage, MyUserDetailsService.MyUserDetails userDetails) {

        Optional<User> isAdmin = userRepository.findByPhoneId(userDetails.getPhoneId());

        // 관리자만 글을 작성할 수 있다.
        if (isAdmin.isEmpty() || isAdmin.get().getUserRoles().stream()
                .noneMatch(userRole -> userRole.getRole().getRoleName() == RoleName.ROLE_ADMIN)) {

            return ResponseEntity.status(403)
                    .body(CustomAPIResponse.createFailWithout(403, "글 작성 권한이 없습니다."));
        }

        // 멘토가 존재하지 않는다면, learningMentorRepository에 추가
        // 이미 존재한다면, 그 멘토를 가져온다
        LearningMentor mentor;
        Optional<LearningMentor> isExist=learningMentorRepository.findByMentorName(learningWriteRequestDTO.getMentorName());
        if(isExist.isEmpty()){
            mentor= LearningMentor.builder()
                    .mentorName(learningWriteRequestDTO.getMentorName())
                    .introduce(learningWriteRequestDTO.getIntroduce())
                    .build();
            learningMentorRepository.save(mentor);
        }else {
            mentor = isExist.get();
            mentor.changeIntroduce(learningWriteRequestDTO.getIntroduce());
        }

        String dateStr = learningWriteRequestDTO.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 M월 d일 a h시 m분");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String imageUrl = null;
        if (learningImage != null && !learningImage.isEmpty()) {
            try {
                imageUrl = s3Service.uploadFile(learningImage);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(CustomAPIResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미지 업로드를 실패했습니다."));
            }
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
                .imageUrl(imageUrl)
                .learningMentor(mentor)
                .build();

        learningRepository.save(newLearning);

        return ResponseEntity.status(201)
                .body(CustomAPIResponse.createSuccess(201, null, "게시글을 성공적으로 작성하였습니다."));

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> modifyPost(Long learningId, LearningWriteRequestDTO learningWriteRequestDTO, MultipartFile learningImage, MyUserDetailsService.MyUserDetails userDetails) {

        Optional<User> isAdmin = userRepository.findByPhoneId(userDetails.getPhoneId());
        Optional<Learning> findLearning = learningRepository.findById(learningId);

        // 관리자만 글을 수정할 수 있다.
        if (isAdmin.isEmpty() || isAdmin.get().getUserRoles().stream()
                .noneMatch(userRole -> userRole.getRole().getRoleName() == RoleName.ROLE_ADMIN)) {

            return ResponseEntity.status(403)
                    .body(CustomAPIResponse.createFailWithout(403, "글 수정 권한이 없습니다."));
        }

        // 멘토가 존재하지 않는다면, learningMentorRepository에 추가
        // 이미 존재한다면, 그 멘토를 가져온다
        LearningMentor mentor;
        Optional<LearningMentor> isExist=learningMentorRepository.findByMentorName(learningWriteRequestDTO.getMentorName());
        if(isExist.isEmpty()){
            mentor= LearningMentor.builder()
                    .mentorName(learningWriteRequestDTO.getMentorName())
                    .introduce(learningWriteRequestDTO.getIntroduce())
                    .build();
            learningMentorRepository.save(mentor);
        }else {
            mentor = isExist.get();
            mentor.changeIntroduce(learningWriteRequestDTO.getIntroduce());
        }

        Learning learning = findLearning.get();

        if (isAdmin.isEmpty())  {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 사용자입니다."));
        } else if (findLearning.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        String dateStr = learningWriteRequestDTO.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 M월 d일 a h시 m분");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 총 모집 인원이 현재 모집된 인원보다 적으면 에러
        if (learningWriteRequestDTO.getTotalCount() < learning.getRecruitmentCount()) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "현재 모집된 인원보다 적은 인원을 모집할 수 없습니다."));
        }

        String imageUrl = null;
        if (learningImage != null && !learningImage.isEmpty()) {
            try {
                imageUrl = s3Service.uploadFile(learningImage);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(CustomAPIResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미지 업로드를 실패했습니다."));
            }
        }

        learning.changeTitle(learningWriteRequestDTO.getTitle());
        learning.changeDate(date);
        learning.changeLocate(learningWriteRequestDTO.getLocate());
        learning.changeCategory(learningWriteRequestDTO.getCategory());
        learning.changeContent(learningWriteRequestDTO.getContent());
        learning.changeImageUrl(imageUrl);
        learning.changeTotalCount(learningWriteRequestDTO.getTotalCount());
        learning.changeLearningMentor(mentor);


        if (learning.getRecruitmentCount() == learning.getTotalCount()) {
            learning.changeIsRecruit(IsRecruit.FALSE);
        }

        if ((learning.getIsRecruit() == IsRecruit.FALSE) && (learning.getRecruitmentCount() < learning.getTotalCount())) {
            learning.changeIsRecruit(IsRecruit.TRUE);
        }

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, null, "게시글을 성공적으로 수정하였습니다."));

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
                    .mentorName(learning.getLearningMentor().getMentorName())
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
                .mentorName(findLearning.get().getLearningMentor().getMentorName())
                .introduce(findLearning.get().getLearningMentor().getIntroduce())
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
        } else if (learningFilteringDTO.getDistrictList() != null && !learningFilteringDTO.getDistrictList().isEmpty()) {
            // state가 없는데 district가 있는 경우 오류 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CustomAPIResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "State가 선택되지 않았습니다."));
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
                    .mentorName(result.getLearningMentor().getMentorName())
                    .build();
            learningResponse.add(response);
        }

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, learningResponse, "조건에 맞는 게시글들을 성공적으로 불러왔습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> recruitLearning(Long learningId, MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        Optional<Learning> findLearning = learningRepository.findById(learningId);

        if (findLearning.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        Learning learning = findLearning.get();

        if (learning.getUser().getPhoneId().equals(phoneId)) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "내가 올린 게시글에 참가할 수 없습니다."));
        }

        if (learning.getIsRecruit() == IsRecruit.FALSE) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "인원이 마감되었습니다."));
        } else if (findLearning.get().getIsCompleted() == IsCompleted.TRUE) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "모집 완료된 활동입니다."));
        }

        Optional<LearningApply> findLearningApply = learningApplyRepository.findByUserPhoneIdAndLearningLearningId(phoneId, learningId);

        if (findLearningApply.isEmpty()) {
            learning.changeRecruitmentCount(learning.getRecruitmentCount() + 1);

            LearningApply newApply = LearningApply.builder()
                    .learning(findLearning.get())
                    .user(user)
                    .build();

            learningApplyRepository.save(newApply);
        } else {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "이미 신청한 활동입니다."));
        }


        if (learning.getRecruitmentCount() == learning.getTotalCount()) {
            learning.changeIsRecruit(IsRecruit.FALSE);
        }

        LearningListDTO.LearningResponse learningResponse = LearningListDTO.LearningResponse.builder()
                .category(findLearning.get().getCategory())
                .title(findLearning.get().getTitle())
                .date(findLearning.get().getDate())
                .locate(findLearning.get().getLocate())
                .imageUrl(findLearning.get().getImageUrl())
                .mentorName(findLearning.get().getLearningMentor().getMentorName())
                .build();

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, learningResponse, "활동 신청이 완료되었습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> cancelLearning(Long learningId, MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        // 놀이터 게시글이 DB에 존재하는가?
        Optional<Learning> findLearning = learningRepository.findById(learningId);
        if (findLearning.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }
        Learning learning = findLearning.get();

        // 존재하는 신청정보인가?
        Optional<LearningApply> findApply = learningApplyRepository.findByUserPhoneIdAndLearningLearningId(phoneId, learningId);
        if (findApply.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 신청 정보입니다."));
        }

        // 게시글 작성자와 현재 작성자가 같다면 참가 취소 불가
        if (learning.getUser().getPhoneId().equals(phoneId)) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "내가 올린 게시글에 참가 취소할 수 없습니다."));
        }

        // 해당 참가자의 참가를 취소하고, 해당 게시글의 인원을 한 명 줄인다.
        // 만약 정원이 다 찬 경우에서 취소한다면, 모집 완료를 모집 중으로 바꾼다.
        if (learning.getIsRecruit() == IsRecruit.FALSE) {
            learning.changeIsRecruit(IsRecruit.TRUE);
        }
        learning.changeRecruitmentCount(learning.getRecruitmentCount() - 1);

        learningApplyRepository.delete(findApply.get());

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, null, "활동 신청이 취소되었습니다."));

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> scrapLearning(Long learningId, MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        Optional<Learning> findLearning = learningRepository.findById(learningId);
        if (findLearning.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        Optional<LearningScrap> findLearningScrap = learningScrapRepository.findByUserPhoneIdAndLearningLearningId(phoneId, learningId);

        if (findLearningScrap.isEmpty()) {
            LearningScrap newScrap = LearningScrap.builder()
                    .learning(findLearning.get())
                    .user(user)
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
    public ResponseEntity<CustomAPIResponse<?>> cancelScrap(Long learningId, MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();

        Optional<Learning> findLearning = learningRepository.findById(learningId);
        if (findLearning.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        Optional<LearningScrap> findLearningScrap = learningScrapRepository.findByUserPhoneIdAndLearningLearningId(phoneId, learningId);

        if (findLearningScrap.isPresent()) {
            learningScrapRepository.delete(findLearningScrap.get());

            return ResponseEntity.status(200)
                    .body(CustomAPIResponse.createSuccess(200, null, "스크랩이 취소되었습니다."));
        }else {

            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400,  "스크랩하지 않은 게시글입니다."));
        }
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> recruitedLearnings(MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));
        Long userId = user.getUserId();

        List<LearningApply> learningApplies = learningApplyRepository.findAllByUserUserId(userId);
        List<LearningListDTO.LearningResponse> learningResponses = new ArrayList<>();

        for (int i = 0; i < learningApplies.size(); i++) {
            Learning learning = learningApplies.get(i).getLearning();

            learningResponses.add(LearningListDTO.LearningResponse.builder()
                    .category(learning.getCategory())
                    .title(learning.getTitle())
                    .date(learning.getDate())
                    .imageUrl(learning.getImageUrl())
                    .mentorName(learning.getLearningMentor().getMentorName())
                    .isCompleted(learning.getIsCompleted())
                    .build());
        }

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, learningResponses, "신청한 게시글 목록을 성공적으로 불러왔습니다."));

    }

    public ResponseEntity<CustomAPIResponse<?>> writeLearningReview(LearningReviewRequestDto learningReviewRequestDto, MyUserDetailsService.MyUserDetails userDetails) {
        Learning learning=learningRepository.findById(learningReviewRequestDto.getLearningId()).orElseThrow();
        Optional<LearningApply> learningApply=learningApplyRepository.findByUserPhoneIdAndLearningLearningId(userDetails.getPhoneId(),learningReviewRequestDto.getLearningId());

                // 내가 신청하지 않은 활동이라면 리뷰를 작성할 수 없다.
                if(!(learningApply.isPresent())) {
                    CustomAPIResponse<Object> failResponse = CustomAPIResponse
                            .createFailWithout(HttpStatus.FORBIDDEN.value(), "신청하지 않은 배움입니다.");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(failResponse);
                }

                // 만약 아직 완료되지 않은 활동이라면 리뷰를 작성할 수 없다.
                if(learning.getIsCompleted().equals(IsCompleted.FALSE)){
                    CustomAPIResponse<Object> failResponse=CustomAPIResponse
                            .createFailWithout(HttpStatus.FORBIDDEN.value(), "아직 완료되지 않은 활동입니다.");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(failResponse);
                }
        LearningReview learningReview=LearningReview.builder()
                .content(learningReviewRequestDto.getContent())
                .rate(learningReviewRequestDto.getRate())
                .learningMentor(learning.getLearningMentor())
                .learning(learning)
                .user(userDetails.getUser())
                .build();
                learningReviewRepository.save(learningReview);

                CustomAPIResponse<Object> successResponse = CustomAPIResponse
                        .createSuccess(HttpStatus.OK.value(), null, "후기가 작성되었습니다.");
                return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> completeLearning(Long learningId, MyUserDetailsService.MyUserDetails userDetails) {
        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));
        Optional<Learning> findLearning = learningRepository.findById(learningId);
        Learning learning = findLearning.get();
        Date date = learning.getDate();
        Date now = new Date();

        if (!date.before(now)) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "활동 날짜가 되지 않았습니다.."));

        }

        if (!Objects.equals(user.getPhoneId(), learning.getUser().getPhoneId())) {
            return ResponseEntity.status(403)
                    .body(CustomAPIResponse.createFailWithout(403, "본인만 게시글을 삭제할 수 있습니다."));
        }
        learning.changeIsRecruit(IsRecruit.FALSE);
        learning.changeComplete(IsCompleted.TRUE);

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, null, "활동을 완료했습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getMyScrapLearnings(MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));
        Long userId = user.getUserId();

        List<LearningScrap> learningScraps = learningScrapRepository.findAllByUserUserId(userId);
        List<LearningListDTO.LearningResponse> learningResponses = new ArrayList<>();

        for (int i = 0; i < learningScraps.size(); i++) {
            Learning learning = learningScraps.get(i).getLearning();

            learningResponses.add(LearningListDTO.LearningResponse.builder()
                    .category(learning.getCategory())
                    .title(learning.getTitle())
                    .date(learning.getDate())
                    .imageUrl(learning.getImageUrl())
                    .isCompleted(learning.getIsCompleted())
                    .build());
        }

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, learningResponses, "스크랩한 게시글 목록을 성공적으로 불러왔습니다."));

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> commentLearning(Long learningId, LearningCommentWriteRequestDTO learningCommentWriteRequestDTO, MyUserDetailsService.MyUserDetails userDetails) {

        // 댓글 작성자가 존재하는가?
        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        Optional<Learning> findLearning = learningRepository.findById(learningId);
        if (findLearning.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "게시글을 찾을 수 없습니다."));
        }

        String dateStr = learningCommentWriteRequestDTO.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 M월 d일 a h시 m분");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LearningComment parentComment = null;

        if (learningCommentWriteRequestDTO.getParentCommentId() != null) {
            Optional<LearningComment> findParentComment = learningCommentRepository.findByLearningCommentId(learningCommentWriteRequestDTO.getParentCommentId());
            parentComment = findParentComment.get();
        }

        LearningComment newComment = LearningComment.builder()
                .user(user)
                .learning(findLearning.get())
                .content(learningCommentWriteRequestDTO.getContent())
                .date(date)
                .parent(parentComment)
                .build();

        learningCommentRepository.save(newComment);

        return ResponseEntity.status(201)
                .body(CustomAPIResponse.createSuccess(201, null, "댓글을 작성하였습니다."));

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getAllComments(Long learningId) {

        List<LearningComment> learningComments = learningCommentRepository.findAllByLearningLearningId(learningId);
        List<CommentListDTO.CommentResponse> commentResponses = new ArrayList<>();

        Optional<Learning> findLearning = learningRepository.findById(learningId);
        if (findLearning.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "게시글을 찾을 수 없습니다."));
        }

        for (LearningComment learningComment : learningComments) {
            Long parentCommentId = 0L;

            if (learningComment.getParent() != null) {
                parentCommentId = learningComment.getParent().getLearningCommentId();
            }

            commentResponses.add(CommentListDTO.CommentResponse.builder()
                    .content(learningComment.getContent())
                    .date(learningComment.getDate())
                    .nickname(learningComment.getUser().getNickname())
                    .commentId(learningComment.getLearningCommentId())
                    .parentCommentId(parentCommentId)
                    .build());
        }

        return ResponseEntity.status(201)
                .body(CustomAPIResponse.createSuccess(201, commentResponses, "해당 게시글의 댓글과 답글을 불러왔습니다."));

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> deleteComment(Long commentId, MyUserDetailsService.MyUserDetails userDetails) {
        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        Optional<LearningComment> findComment = learningCommentRepository.findById(commentId);
        if (findComment.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 댓글입니다."));
        }

        LearningComment comment = findComment.get();

        if (!comment.getUser().getPhoneId().equals(phoneId)) {
            return ResponseEntity.status(403)
                    .body(CustomAPIResponse.createFailWithout(403, "본인이 작성한 댓글만 삭제할 수 있습니다."));
        }

        learningCommentRepository.delete(comment);

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, null, "댓글 삭제를 성공했습니다."));


    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getMentorReview(MentorReviewRequestDto mentorReviewRequestDto) {
        String mentorName= mentorReviewRequestDto.getMentorName();
        Optional<LearningMentor> mentor= learningMentorRepository.findByMentorName(mentorName);

        // 해당 멘토가 존재하지 않는다면
        if (mentor.isEmpty()) {
            CustomAPIResponse<Object> failResponse = CustomAPIResponse
                    .createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 멘토입니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failResponse);
        }

        List<LearningReview> learningReviews=learningReviewRepository.findByLearningMentor(mentor.get());

        // 평균 점수 계산
        double averageRate=0;
        if (!learningReviews.isEmpty()) {
            double sumRate = learningReviews.stream()
                    .mapToDouble(LearningReview::getRate)
                    .sum();
            averageRate = sumRate / learningReviews.size();
        }

        List<LearningReview> recentReviews=learningReviews.stream()
                //Comparator.comparing(LearningReview::getCreatedAt)는 getCreatedAt 메서드를 기준으로 정렬하는 Comparator를 생성
                .sorted(Comparator.comparing(LearningReview::getCreatedAt)
                        .reversed())
                .limit(4)
                .toList();


        List<GetMentorReviewResponseDto.GetReview> reviewList=recentReviews.stream()
                .map(learningReview -> GetMentorReviewResponseDto.GetReview.builder()
                        .writer(learningReview.getUser().getNickname())
                        .content(learningReview.getContent())
                        .rate(learningReview.getRate())
                        .build()).collect(Collectors.toList());


        GetMentorReviewResponseDto.FinalResponseDto res=GetMentorReviewResponseDto.FinalResponseDto.builder()
                .reviewList(reviewList)
                .averageRate(averageRate)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CustomAPIResponse.createSuccess(HttpStatus.OK.value(), res, "멘토 리뷰 조회에 성공하였습니다."));

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getApplicant(Long learningId, MyUserDetailsService.MyUserDetails userDetails) {

        // 인증된 사용자 정보에서 phoneId를 가져온다.
        String phoneId = userDetails.getPhoneId();
        User admin = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));


        if (admin.getUserRoles().stream()
                .noneMatch(userRole -> userRole.getRole().getRoleName() == RoleName.ROLE_ADMIN)) {

            return ResponseEntity.status(403)
                    .body(CustomAPIResponse.createFailWithout(403, "배움터는 관리자만 참가자를 볼 수 있습니다."));
        }

        Optional<Learning> findLearning = learningRepository.findById(learningId);
        if (findLearning.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        List<LearningApply> learningApplies = learningApplyRepository.findAllByLearningLearningId(learningId);
        List<ApplicantListDTO.ApplicantResponse> applicantResponses = new ArrayList<>();

        for (LearningApply learningApply : learningApplies) {
            applicantResponses.add(ApplicantListDTO.ApplicantResponse.builder()
                    .nickname(learningApply.getUser().getNickname())
                    .phoneId(learningApply.getUser().getPhoneId())
                    .profileImage(learningApply.getUser().getProfileImage())
                    .build());
        }


        return ResponseEntity.status(201)
                .body(CustomAPIResponse.createSuccess(201, applicantResponses, "참가 신청자 목록을 불러왔습니다."));

    }


}
