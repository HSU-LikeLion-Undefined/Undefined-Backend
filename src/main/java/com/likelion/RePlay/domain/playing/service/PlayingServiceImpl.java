package com.likelion.RePlay.domain.playing.service;

import com.likelion.RePlay.domain.playing.entity.PlayingReview;
import com.likelion.RePlay.domain.playing.entity.PlayingScrap;
import com.likelion.RePlay.domain.playing.repository.*;
import com.likelion.RePlay.domain.playing.web.dto.PlayingFilteringDTO;
import com.likelion.RePlay.domain.playing.web.dto.PlayingListDTO;
import com.likelion.RePlay.domain.playing.web.dto.PlayingReviewRequestDto;
import com.likelion.RePlay.domain.playing.web.dto.PlayingWriteRequestDTO;

import com.likelion.RePlay.domain.playing.entity.PlayingComment;
import com.likelion.RePlay.domain.playing.web.dto.*;
import com.likelion.RePlay.domain.playing.entity.Playing;
import com.likelion.RePlay.domain.playing.entity.PlayingApply;
import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.domain.user.repository.UserRepository;
import com.likelion.RePlay.global.amazon.S3.S3Service;
import com.likelion.RePlay.global.enums.District;
import com.likelion.RePlay.global.enums.IsCompleted;
import com.likelion.RePlay.global.enums.IsRecruit;
import com.likelion.RePlay.global.enums.State;
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
public class PlayingServiceImpl implements PlayingService {

    private final UserRepository userRepository;
    private final PlayingRepository playingRepository;
    private final PlayingApplyRepository playingApplyRepository;
    private final PlayingScrapRepository playingScrapRepository;
    private final PlayingReviewRepository playingReviewRepository;
    private final PlayingCommentRepository playingCommentRepository;
    private final S3Service s3Service;

    @Override
    public ResponseEntity<CustomAPIResponse<?>> writePost(PlayingWriteRequestDTO playingWriteRequestDTO, MultipartFile playingImage, MyUserDetailsService.MyUserDetails userDetails) {

        // 인증된 사용자 정보에서 phoneId를 가져온다.
        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        String imageUrl = null;
        if (playingImage != null && !playingImage.isEmpty()) {
            try {
                imageUrl = s3Service.uploadFile(playingImage);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(CustomAPIResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미지 업로드를 실패했습니다."));
            }
        }

        String dateStr = playingWriteRequestDTO.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 M월 d일 a h시 m분");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 존재한다면 게시글을 DB에 저장한다. 자기소개는 User 엔티티에 저장한다.
        user.changeIntroduce(playingWriteRequestDTO.getIntroduce());
        userRepository.save(user);

        Playing newPlaying = Playing.builder()
                .user(user)
                .title(playingWriteRequestDTO.getTitle())
                .category(playingWriteRequestDTO.getCategory())
                .date(date)
                .isRecruit(IsRecruit.TRUE)
                .isCompleted(IsCompleted.FALSE)
                .totalCount(playingWriteRequestDTO.getTotalCount())
                .recruitmentCount(0L)
                .content(playingWriteRequestDTO.getContent())
                .cost(playingWriteRequestDTO.getCost())
                .costDescription(playingWriteRequestDTO.getCostDescription())
                .locate(playingWriteRequestDTO.getLocate())
                .latitude(playingWriteRequestDTO.getLatitude())
                .longitude(playingWriteRequestDTO.getLongitude())
                .state(playingWriteRequestDTO.getState())
                .district(playingWriteRequestDTO.getDistrict())
                .imageUrl(imageUrl)
                .build();

        playingRepository.save(newPlaying);

        return ResponseEntity.status(201)
                .body(CustomAPIResponse.createSuccess(201, null, "게시글을 성공적으로 작성하였습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> modifyPost(Long playingId, PlayingWriteRequestDTO playingWriteRequestDTO, MultipartFile playingImage, MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        // 게시글 존재 여부 확인
        Optional<Playing> findPlaying = playingRepository.findById(playingId);
        if (findPlaying.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        Playing playing = findPlaying.get();

        // 사용자가 해당 게시글의 작성자인지 확인
        if (!playing.getUser().getPhoneId().equals(phoneId)) {
            return ResponseEntity.status(403)
                    .body(CustomAPIResponse.createFailWithout(403, "본인의 글만 수정할 수 있습니다."));
        }

        // 현재 모집된 인원이 있다면 수정 불가
        if (playing.getRecruitmentCount() > 0) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "모집된 인원이 있으면 게시글을 수정할 수 없습니다."));
        }

        String dateStr = playingWriteRequestDTO.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 M월 d일 a h시 m분");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String imageUrl = null;
        if (playingImage != null && !playingImage.isEmpty()) {
            try {
                imageUrl = s3Service.uploadFile(playingImage);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(CustomAPIResponse.createFailWithout(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미지 업로드를 실패했습니다."));
            }
        }

        user.changeIntroduce(playingWriteRequestDTO.getIntroduce());
        playing.changeTitle(playingWriteRequestDTO.getTitle());
        playing.changeDate(date);
        playing.changeLocate(playingWriteRequestDTO.getLocate());
        playing.changeCategory(playingWriteRequestDTO.getCategory());
        playing.changeContent(playingWriteRequestDTO.getContent());
        playing.changeImageUrl(imageUrl);
        playing.changeTotalCount(playingWriteRequestDTO.getTotalCount());
        playing.changeCost(Long.valueOf(playingWriteRequestDTO.getCost()));
        playing.changeCostDescription(playingWriteRequestDTO.getCostDescription());

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, null, "게시글을 성공적으로 수정하였습니다."));

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getAllPosts() {
        List<Playing> playings = playingRepository.findAll();

        List<PlayingListDTO.PlayingResponse> playingResponses = new ArrayList<>();

        for (Playing playing : playings) {
            playingResponses.add(PlayingListDTO.PlayingResponse.builder()
                    .category(playing.getCategory())
                    .title(playing.getTitle())
                    .state(playing.getState())
                    .district(playing.getDistrict())
                    .date(playing.getDate())
                    .totalCount(playing.getTotalCount())
                    .recruitmentCount(playing.getRecruitmentCount())
                    .imageUrl(playing.getImageUrl())
                    .build());
        }

        // 사용자에게 반환하기위한 최종 데이터
        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, playingResponses, "게시글 목록을 성공적으로 불러왔습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getPost(Long playingId) {

        Optional<Playing> findPlaying = playingRepository.findById(playingId);

        if (findPlaying.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        User user = findPlaying.get().getUser();

        PlayingListDTO.PlayingResponse playingResponse = PlayingListDTO.PlayingResponse.builder()
                .nickname(user.getNickname())
                .introduce(user.getIntroduce())
                .category(findPlaying.get().getCategory())
                .title(findPlaying.get().getTitle())
                .date(findPlaying.get().getDate())
                .locate(findPlaying.get().getLocate())
                .state(findPlaying.get().getState())
                .district(findPlaying.get().getDistrict())
                .totalCount(findPlaying.get().getTotalCount())
                .recruitmentCount(findPlaying.get().getRecruitmentCount())
                .content(findPlaying.get().getContent())
                .cost(findPlaying.get().getCost())
                .costDescription(findPlaying.get().getCostDescription())
                .imageUrl(findPlaying.get().getImageUrl())
                .build();

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, playingResponse, "특정 게시글을 성공적으로 불러왔습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> filtering(PlayingFilteringDTO playingFilteringDTO) {

        // 1. 모든 게시글을 DB에서 불러오기
        List<Playing> allPlayings = playingRepository.findAll();

        // 2. 문자열로 된 dateList를 Date 객체로 변환
        List<Date> parsedDates = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM d", Locale.KOREA); // "8월 2일" 형식으로 변환
        try {
            for (String dateString : playingFilteringDTO.getDateList()) {
                // 연도와 시간을 설정하지 않고 날짜와 월만 설정
                Date date = formatter.parse(dateString);
                parsedDates.add(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CustomAPIResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "날짜 형식이 잘못되었습니다."));
        }

        // 3. Date 조건과 일치하는 게시글만 남기기 (Date가 null일 경우 필터링하지 않음)
        List<Playing> filteredByDate = allPlayings;
        if (!parsedDates.isEmpty()) {
            filteredByDate = filteredByDate.stream()
                    .filter(playing -> {
                        LocalDate playingDate = playing.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        return parsedDates.stream().anyMatch(date -> {
                            LocalDate filterDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            return playingDate.getMonth() == filterDate.getMonth() && playingDate.getDayOfMonth() == filterDate.getDayOfMonth();
                        });
                    })
                    .collect(Collectors.toList());
        }

        // 4. State 및 District 조건과 일치하는 게시글만 남기기 (Null일 경우 필터링하지 않음)
        List<Playing> filteredByLocation = filteredByDate;
        if (playingFilteringDTO.getStateList() != null && !playingFilteringDTO.getStateList().isEmpty()) {
            filteredByLocation = filteredByLocation.stream()
                    .filter(playing -> {
                        boolean matches = false;
                        for (int i = 0; i < playingFilteringDTO.getStateList().size(); i++) {
                            State state = playingFilteringDTO.getStateList().get(i);
                            District district = (playingFilteringDTO.getDistrictList() != null && playingFilteringDTO.getDistrictList().size() > i)
                                    ? playingFilteringDTO.getDistrictList().get(i)
                                    : null;
                            if (playing.getState().equals(state) &&
                                    (district == null || district.equals(District.ALL) || playing.getDistrict().equals(district))) {
                                matches = true;
                                break;
                            }
                        }
                        return matches;
                    })
                    .collect(Collectors.toList());
        } else if (playingFilteringDTO.getDistrictList() != null && !playingFilteringDTO.getDistrictList().isEmpty()) {
            // state가 없는데 district가 있는 경우 오류 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CustomAPIResponse.createFailWithout(HttpStatus.BAD_REQUEST.value(), "State가 선택되지 않았습니다."));
        }

        // 5. Category 조건과 일치하는 게시글만 남기기 (Null일 경우 필터링하지 않음)
        List<Playing> filteredByCategory = filteredByLocation;
        if (playingFilteringDTO.getCategory() != null) {
            filteredByCategory = filteredByCategory.stream()
                    .filter(playing -> playing.getCategory().equals(playingFilteringDTO.getCategory()))
                    .collect(Collectors.toList());
        }

        // 6. 모든 조건에 부합하는 게시글만 ResponseBody로 전달하기
        List<PlayingListDTO.PlayingResponse> playingResponse = new ArrayList<>();
        for (Playing result : filteredByCategory) {
            PlayingListDTO.PlayingResponse response = PlayingListDTO.PlayingResponse.builder()
                    .category(result.getCategory())
                    .title(result.getTitle())
                    .state(result.getState())
                    .district(result.getDistrict())
                    .date(result.getDate())
                    .totalCount(result.getTotalCount())
                    .recruitmentCount(result.getRecruitmentCount())
                    .imageUrl(result.getImageUrl())
                    .build();
            playingResponse.add(response);
        }

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, playingResponse, "조건에 맞는 게시글들을 성공적으로 불러왔습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> recruitPlaying(Long playingId, MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        // 놀이터 게시글이 DB에 존재하는가?
        Optional<Playing> findPlaying = playingRepository.findById(playingId);

        // 존재하지 않는다면 오류 반환
        if (findPlaying.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        Playing playing = findPlaying.get();

        // 게시글 작성자와 현재 작성자가 같다면 참가 신청 불가
        if (playing.getUser().getPhoneId().equals(phoneId)) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "내가 올린 게시글에 참가할 수 없습니다."));
        }

        // 인원이 다 차거나 모집 완료된 활동일 경우 오류 반환
        if (playing.getIsRecruit() == IsRecruit.FALSE) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "인원이 마감되었습니다."));
        } else if (playing.getIsCompleted() == IsCompleted.TRUE) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "모집 완료된 활동입니다."));
        }

        Optional<PlayingApply> findPlayingApply = playingApplyRepository.findByUserPhoneIdAndPlayingPlayingId(phoneId, playingId);

        // 신청하지 않은 활동일 경우
        // 해당 게시글 신청 정보에 해당 유저의 정보를 추가한다.
        if (findPlayingApply.isEmpty()) {
            // 해당 게시글에 모집 인원을 추가한다.
            playing.changeRecruitmentCount(playing.getRecruitmentCount() + 1);

            PlayingApply newApply = PlayingApply.builder()
                    .playing(playing)
                    .user(user)
                    .build();

            playingApplyRepository.save(newApply);
        } else {
            // 이미 신청한 활동일 경우, 오류 반환
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "이미 신청한 활동입니다."));
        }

        // 모집인원이 다 찼을 경우, 모집중 -> 모집완료로 바꾼다.
        if (playing.getRecruitmentCount() == playing.getTotalCount()) {
            playing.changeIsRecruit(IsRecruit.FALSE);
        }

        PlayingListDTO.PlayingResponse playingResponse = PlayingListDTO.PlayingResponse.builder()
                .category(playing.getCategory())
                .title(playing.getTitle())
                .date(playing.getDate())
                .locate(playing.getLocate())
                .cost(playing.getCost())
                .imageUrl(playing.getImageUrl())
                .build();

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, playingResponse, "활동 신청이 완료되었습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> cancelPlaying(Long playingId, MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        // 놀이터 게시글이 DB에 존재하는가?
        Optional<Playing> findPlaying = playingRepository.findById(playingId);
        if (findPlaying.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }
        Playing playing = findPlaying.get();

        // 존재하는 신청정보인가?
        Optional<PlayingApply> findApply = playingApplyRepository.findByUserPhoneIdAndPlayingPlayingId(phoneId, playingId);
        if (findApply.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 신청 정보입니다."));
        }

        // 게시글 작성자와 현재 작성자가 같다면 참가 취소 불가
        if (playing.getUser().getPhoneId().equals(phoneId)) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "내가 올린 게시글에 참가 취소할 수 없습니다."));
        }

        // 해당 참가자의 참가를 취소하고, 해당 게시글의 인원을 한 명 줄인다.
        // 만약 정원이 다 찬 경우에서 취소한다면, 모집 완료를 모집 중으로 바꾼다.
        if (playing.getIsRecruit() == IsRecruit.FALSE) {
            playing.changeIsRecruit(IsRecruit.TRUE);
        }
        playing.changeRecruitmentCount(playing.getRecruitmentCount() - 1);

        playingApplyRepository.delete(findApply.get());

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, null, "활동 신청이 취소되었습니다."));
    }

    // 스크랩
    @Override
    public ResponseEntity<CustomAPIResponse<?>> scrapPlayings(Long playingId, MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        // 놀이터 게시글이 DB에 존재하는가?
        Optional<Playing> findPlaying = playingRepository.findById(playingId);
        if (findPlaying.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        Optional<PlayingScrap> findPlayingScrap = playingScrapRepository.findByUserPhoneIdAndPlayingPlayingId(phoneId, playingId);

        // 스크랩하지 않은 활동일 경우
        // 해당 게시글 신청 정보에 해당 유저의 정보를 추가한다.
        if (findPlayingScrap.isEmpty()) {
            PlayingScrap newScrap = PlayingScrap.builder()
                    .playing(findPlaying.get())
                    .user(user)
                    .build();

            playingScrapRepository.save(newScrap);

            return ResponseEntity.status(200)
                    .body(CustomAPIResponse.createSuccess(200, null, "스크랩되었습니다."));
        } else {

            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "이미 스크랩했습니다."));
        }

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> cancelScrap(Long playingId, MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();

        // 놀이터 게시글이 DB에 존재하는가?
        Optional<Playing> findPlaying = playingRepository.findById(playingId);
        if (findPlaying.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        Optional<PlayingScrap> findPlayingScrap = playingScrapRepository.findByUserPhoneIdAndPlayingPlayingId(phoneId, playingId);

        // 스크랩한 않은 활동일 경우 취소한다.
        if (findPlayingScrap.isPresent()) {
            playingScrapRepository.delete(findPlayingScrap.get());

            return ResponseEntity.status(200)
                    .body(CustomAPIResponse.createSuccess(200, null, "스크랩이 취소되었습니다."));
        } else {

            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "스크랩하지 않은 게시글입니다."));
        }

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getMyPlayings(MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();

        List<Playing> playings = playingRepository.findAllByUserPhoneId(phoneId);
        List<PlayingListDTO.PlayingResponse> playingResponses = new ArrayList<>();

        for (Playing playing : playings) {
            playingResponses.add(PlayingListDTO.PlayingResponse.builder()
                    .category(playing.getCategory())
                    .title(playing.getTitle())
                    .date(playing.getDate())
                    .isCompleted(playing.getIsCompleted())
                    .imageUrl(playing.getImageUrl())
                    .build());
        }

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, playingResponses, "게시글 목록을 성공적으로 불러왔습니다."));

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> deleteMyPlaying(Long playingId, MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        // 게시글 존재 여부 확인
        Optional<Playing> findPlaying = playingRepository.findById(playingId);
        if (findPlaying.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        Playing playing = findPlaying.get();

        // 현재 유저와 게시글 작성자가 동일하지 않다면 삭제 불가
        if (user.getPhoneId() != playing.getUser().getPhoneId()) {
            return ResponseEntity.status(403)
                    .body(CustomAPIResponse.createFailWithout(403, "본인만 게시글을 삭제할 수 있습니다."));
        }

        // 존재하는 게시글이라면 삭제
        playingRepository.delete(playing);

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, null, "게시글 삭제를 성공했습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> completePlaying(Long playingId, MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        Optional<Playing> findPlaying = playingRepository.findById(playingId);
        Playing playing = findPlaying.get();
        Date date = playing.getDate();
        Date now = new Date();

        if (!date.before(now)) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "활동 날짜가 되지 않았습니다.."));

        }

        if (user.getPhoneId() != playing.getUser().getPhoneId()) {
            return ResponseEntity.status(403)
                    .body(CustomAPIResponse.createFailWithout(403, "본인만 게시글을 삭제할 수 있습니다."));
        }

        playing.changeIsRecruit(IsRecruit.FALSE);
        playing.changeIsCompleted(IsCompleted.TRUE);

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, null, "활동을 완료했습니다."));

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> recruitedPlayings(MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));
        Long userId = user.getUserId();

        List<PlayingApply> playingApplies = playingApplyRepository.findAllByUserUserId(userId);
        List<PlayingListDTO.PlayingResponse> playingResponses = new ArrayList<>();

        for (int i = 0; i < playingApplies.size(); i++) {
            Playing playing = playingApplies.get(i).getPlaying();

            playingResponses.add(PlayingListDTO.PlayingResponse.builder()
                    .category(playing.getCategory())
                    .title(playing.getTitle())
                    .date(playing.getDate())
                    .isCompleted(playing.getIsCompleted())
                    .imageUrl(playing.getImageUrl())
                    .build());
        }

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, playingResponses, "신청한 게시글 목록을 성공적으로 불러왔습니다."));

    }

    @Override

    public ResponseEntity<CustomAPIResponse<?>> writePlayingReview(PlayingReviewRequestDto playingReviewRequestDto, MyUserDetailsService.MyUserDetails userDetails) {
        Playing playing= playingRepository.findById(playingReviewRequestDto.getPlayingId()).orElseThrow();
        Optional<PlayingApply> playingApply=playingApplyRepository.findByUserPhoneIdAndPlayingPlayingId(userDetails.getPhoneId(), playingReviewRequestDto.getPlayingId());

        // 내가 신청하지 않은 활동이라면 리뷰를 작성할 수 없다.
        if(!(playingApply.isPresent())){
            CustomAPIResponse<Object> failResponse=CustomAPIResponse
                    .createFailWithout(HttpStatus.FORBIDDEN.value(), "신청하지 않은 놀이글입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(failResponse);

        }
        // 만약 아직 완료되지 않은 활동이라면 리뷰를 작성할 수 없다.
        if(playing.getIsCompleted().equals(IsCompleted.FALSE)){
            CustomAPIResponse<Object> failResponse=CustomAPIResponse
                    .createFailWithout(HttpStatus.FORBIDDEN.value(), "아직 완료되지 않은 활동입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(failResponse);
        }

        PlayingReview playingReview=PlayingReview.builder()
                .content(playingReviewRequestDto.getContent())
                .rate(playingReviewRequestDto.getRate())
                .playing(playing)
                .user(userDetails.getUser())
                .build();
        playingReviewRepository.save(playingReview);

        CustomAPIResponse<Object> successResponse = CustomAPIResponse
                .createSuccess(HttpStatus.OK.value(), null, "후기가 작성되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(successResponse);


    }


    // 스크랩한 놀이터 게시글 조회
    @Override
    public ResponseEntity<CustomAPIResponse<?>> getMyScrapPlaying(MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));
        Long userId = user.getUserId();

        List<PlayingScrap> playingScraps = playingScrapRepository.findAllByUserUserId(userId);
        List<PlayingListDTO.PlayingResponse> playingResponses = new ArrayList<>();

        for (int i = 0; i < playingScraps.size(); i++) {
            Playing playing = playingScraps.get(i).getPlaying();

            playingResponses.add(PlayingListDTO.PlayingResponse.builder()
                    .category(playing.getCategory())
                    .title(playing.getTitle())
                    .date(playing.getDate())
                    .imageUrl(playing.getImageUrl())
                    .isCompleted(playing.getIsCompleted())
                    .build());
        }

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, playingResponses, "스크랩한 게시글 목록을 성공적으로 불러왔습니다."));

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> commentPlaying(Long playingId, PlayingCommentWriteRequestDTO playingCommentWriteRequestDTO, MyUserDetailsService.MyUserDetails userDetails) {

        // 댓글 작성자가 존재하는가?
        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        // DB에서 해당 게시글을 찾는다.
        Optional<Playing> findPlaying = playingRepository.findById(playingId);
        if (findPlaying.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "게시글을 찾을 수 없습니다."));
        }

        String dateStr = playingCommentWriteRequestDTO.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 M월 d일 a h시 m분");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        PlayingComment parentComment = null;

        if (playingCommentWriteRequestDTO.getParentCommentId() != null) {
            // 답글일 경우 부모 댓글을 설정한다
            Optional<PlayingComment> findParentComment = playingCommentRepository.findByPlayingCommentId(playingCommentWriteRequestDTO.getParentCommentId());
            parentComment = findParentComment.get();

        }

        PlayingComment newComment = PlayingComment.builder()
                .user(user)
                .playing(findPlaying.get())
                .content(playingCommentWriteRequestDTO.getContent())
                .date(date)
                .parent(parentComment)
                .build();

        // 댓글 저장
        playingCommentRepository.save(newComment);

        return ResponseEntity.status(201)
                .body(CustomAPIResponse.createSuccess(201, null, "댓글을 작성하였습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getAllComments(Long playingId) {

        List<PlayingComment> playingComments = playingCommentRepository.findAllByPlayingPlayingId(playingId);
        List<CommentListDTO.CommentResponse> commentResponses = new ArrayList<>();

        // DB에서 해당 게시글을 찾는다.
        Optional<Playing> findPlaying = playingRepository.findById(playingId);
        if (findPlaying.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "게시글을 찾을 수 없습니다."));
        }

        for (PlayingComment playingComment : playingComments) {
            Long parentCommentId = 0L;

            if (playingComment.getParent() != null) {
                parentCommentId = playingComment.getParent().getPlayingCommentId();
            }

            commentResponses.add(CommentListDTO.CommentResponse.builder()
                    .content(playingComment.getContent())
                    .date(playingComment.getDate())
                    .nickname(playingComment.getUser().getNickname())
                    .commentId(playingComment.getPlayingCommentId())
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

        Optional<PlayingComment> findComment = playingCommentRepository.findById(commentId);
        if (findComment.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 댓글입니다."));
        }

        PlayingComment comment = findComment.get();

        // 사용자가 해당 게시글의 작성자인지 확인
        if (!comment.getUser().getPhoneId().equals(phoneId)) {
            return ResponseEntity.status(403)
                    .body(CustomAPIResponse.createFailWithout(403, "본인이 작성한 댓글만 삭제할 수 있습니다."));
        }

        playingCommentRepository.delete(comment);

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, null, "댓글 삭제를 성공했습니다."));

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getUserReview(UserReviewRequestDto userReviewRequestDto) {
        String userName= userReviewRequestDto.getUserName();
        Optional<User> isExist= userRepository.findByNickname(userName);

                // 해당 사용자가 존재하지 않는다면
                if (isExist.isEmpty()) {
                    CustomAPIResponse<Object> failResponse = CustomAPIResponse
                            .createFailWithout(HttpStatus.NOT_FOUND.value(), "존재하지 않는 사용자입니다.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failResponse);
                }

                List<PlayingReview> playingReviews=playingReviewRepository.findReviewsByRecipientNickname(isExist.get().getNickname());

                // 평균 점수 계산
                double averageRate=0;
                if (!playingReviews.isEmpty()) {
                    double sumRate = playingReviews.stream()
                            .mapToDouble(PlayingReview::getRate)
                            .sum();
                    averageRate = sumRate / playingReviews.size();
                }

                List<PlayingReview> recentReviews=playingReviews.stream()
                        //Comparator.comparing(PlayingReview::getCreatedAt)는 getCreatedAt 메서드를 기준으로 정렬하는 Comparator를 생성
                        .sorted(Comparator.comparing(PlayingReview::getCreatedAt)
                                .reversed())
                        .limit(4)
                        .toList();

                List<GetUserReviewResponseDto.GetReview> reviewList=recentReviews.stream()
                        .map(playingReview -> GetUserReviewResponseDto.GetReview.builder()
                                .writer(playingReview.getUser().getNickname())
                                .content(playingReview.getContent())
                                .rate(playingReview.getRate())
                               .build()).collect(Collectors.toList());

                GetUserReviewResponseDto.FinalResponseDto res=GetUserReviewResponseDto.FinalResponseDto.builder()
                        .reviewList(reviewList)
                        .averageRate(averageRate)
                        .build();

                return ResponseEntity.status(HttpStatus.OK)
                        .body(CustomAPIResponse.createSuccess(HttpStatus.OK.value(), res, "사용자 리뷰 조회에 성공하였습니다."));

    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> getApplicant(Long playingId, MyUserDetailsService.MyUserDetails userDetails) {

        // 인증된 사용자 정보에서 phoneId를 가져온다.
        String phoneId = userDetails.getPhoneId();
        User writer = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        Optional<Playing> findPlaying = playingRepository.findById(playingId);
        if (findPlaying.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        if (!writer.equals(findPlaying.get().getUser())) {
            return ResponseEntity.status(403)
                    .body(CustomAPIResponse.createFailWithout(403, "작성자만 참가자를 볼 수 있습니다."));
        }

        List<PlayingApply> playingApplies = playingApplyRepository.findAllByPlayingPlayingId(playingId);

        List<ApplicantListDTO.ApplicantResponse> applicantResponses = new ArrayList<>();

        for (PlayingApply playingApply : playingApplies) {
            applicantResponses.add(ApplicantListDTO.ApplicantResponse.builder()
                    .nickname(playingApply.getUser().getNickname())
                    .phoneId(playingApply.getUser().getPhoneId())
                    .profileImage(playingApply.getUser().getProfileImage())
                    .build());
        }

        return ResponseEntity.status(201)
                .body(CustomAPIResponse.createSuccess(201, applicantResponses, "참가 신청자 목록을 불러왔습니다."));
    }


}