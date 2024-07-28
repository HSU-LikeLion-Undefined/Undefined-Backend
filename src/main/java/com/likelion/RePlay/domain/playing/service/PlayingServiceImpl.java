package com.likelion.RePlay.domain.playing.service;

import com.likelion.RePlay.domain.playing.entity.PlayingScrap;
import com.likelion.RePlay.domain.playing.repository.PlayingScrapRepository;
import com.likelion.RePlay.domain.playing.web.dto.PlayingFilteringDTO;
import com.likelion.RePlay.domain.playing.web.dto.PlayingListDTO;
import com.likelion.RePlay.domain.playing.web.dto.PlayingWriteRequestDTO;
import com.likelion.RePlay.domain.playing.entity.Playing;
import com.likelion.RePlay.domain.playing.entity.PlayingApply;
import com.likelion.RePlay.domain.playing.repository.PlayingApplyRepository;
import com.likelion.RePlay.domain.playing.repository.PlayingRepository;
import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.domain.user.repository.UserRepository;
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

    @Override
    public ResponseEntity<CustomAPIResponse<?>> writePost(PlayingWriteRequestDTO playingWriteRequestDTO, MyUserDetailsService.MyUserDetails userDetails) {

        // 인증된 사용자 정보에서 phoneId를 가져온다.
        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

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
                .cost(Long.valueOf(playingWriteRequestDTO.getCost()))
                .costDescription(playingWriteRequestDTO.getCostDescription())
                .locate(playingWriteRequestDTO.getLocate())
                .latitude(playingWriteRequestDTO.getLatitude())
                .longitude(playingWriteRequestDTO.getLongitude())
                .state(playingWriteRequestDTO.getState())
                .district(playingWriteRequestDTO.getDistrict())
                .imageUrl(playingWriteRequestDTO.getImageUrl())
                .build();

        playingRepository.save(newPlaying);

        return ResponseEntity.status(201)
                .body(CustomAPIResponse.createSuccess(201, null, "게시글을 성공적으로 작성하였습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> modifyPost(Long playingId, PlayingWriteRequestDTO playingWriteRequestDTO, MyUserDetailsService.MyUserDetails userDetails) {

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

        String dateStr = playingWriteRequestDTO.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 M월 d일 a h시 m분");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 총 모집 인원이 현재 모집된 인원보다 적으면 에러
        if (playingWriteRequestDTO.getTotalCount() < playing.getRecruitmentCount()) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "현재 모집된 인원보다 적은 인원을 모집할 수 없습니다."));
        }

        user.changeIntroduce(playingWriteRequestDTO.getIntroduce());
        playing.changeTitle(playingWriteRequestDTO.getTitle());
        playing.changeDate(date);
        playing.changeLocate(playingWriteRequestDTO.getLocate());
        playing.changeCategory(playingWriteRequestDTO.getCategory());
        playing.changeContent(playingWriteRequestDTO.getContent());
        playing.changeImageUrl(playingWriteRequestDTO.getImageUrl());
        playing.changeTotalCount(playingWriteRequestDTO.getTotalCount());
        playing.changeCost(Long.valueOf(playingWriteRequestDTO.getCost()));
        playing.changeCostDescription(playingWriteRequestDTO.getCostDescription());

        if (playing.getRecruitmentCount() == playing.getTotalCount()) {
            playing.changeIsRecruit(IsRecruit.FALSE);
        }

        if ((playing.getIsRecruit() == IsRecruit.FALSE) && (playing.getRecruitmentCount() < playing.getTotalCount())) {
            playing.changeIsRecruit(IsRecruit.TRUE);
        }

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
            System.out.println("날짜 필터 : " + filteredByDate.size() + "개 있습니다.");
        } else {
            System.out.println("필터가 적용되지 않았습니다.");
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
            System.out.println("시 및 구 필터 : " + filteredByLocation.size() + "개 있습니다.");
        } else {
            System.out.println("필터가 적용되지 않았습니다.");
        }

        // 5. Category 조건과 일치하는 게시글만 남기기 (Null일 경우 필터링하지 않음)
        List<Playing> filteredByCategory = filteredByLocation;
        if (playingFilteringDTO.getCategory() != null) {
            filteredByCategory = filteredByCategory.stream()
                    .filter(playing -> playing.getCategory().equals(playingFilteringDTO.getCategory()))
                    .collect(Collectors.toList());
            System.out.println("카테고리 필터 : " + filteredByCategory.size() + "개 있습니다.");
        } else {
            System.out.println("필터가 적용되지 않았습니다.");
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

        Optional<PlayingApply> findPlayingApply = playingApplyRepository.findByUserPhoneId(phoneId);

        // 신청하지 않은 활동일 경우
        // 해당 게시글 신청 정보에 해당 유저의 정보를 추가한다.
        if (findPlayingApply.isEmpty()) {
            PlayingApply newApply = PlayingApply.builder()
                    .playing(findPlaying.get())
                    .user(user)
                    .build();

            playingApplyRepository.save(newApply);
        }else {
            // 이미 신청한 활동일 경우, 오류 반환
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "이미 신청한 활동입니다."));
        }

        // 해당 게시글에 모집 인원을 추가한다.
        playing.changeRecruitmentCount(playing.getRecruitmentCount() + 1);

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
        Optional<PlayingApply> findApply = playingApplyRepository.findByUserPhoneId(phoneId);
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

    @Override
    public ResponseEntity<CustomAPIResponse<?>> scrapPlaying(Long playingId, MyUserDetailsService.MyUserDetails userDetails) {

        String phoneId = userDetails.getPhoneId();
        User user = userRepository.findByPhoneId(phoneId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다."));

        // 놀이터 게시글이 DB에 존재하는가?
        Optional<Playing> findPlaying = playingRepository.findById(playingId);
        if (findPlaying.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        Optional<PlayingScrap> findPlayingScrap = playingScrapRepository.findByUserPhoneId(phoneId);

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
        }else {

            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400,  "이미 스크랩했습니다."));
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

        Optional<PlayingScrap> findPlayingScrap = playingScrapRepository.findByUserPhoneId(phoneId);

        // 스크랩한 않은 활동일 경우 취소한다.
        if (findPlayingScrap.isPresent()) {
            playingScrapRepository.delete(findPlayingScrap.get());

            return ResponseEntity.status(200)
                    .body(CustomAPIResponse.createSuccess(200, null, "스크랩이 취소되었습니다."));
        }else {

            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400,  "스크랩하지 않은 게시글입니다."));
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
                    .imageUrl(playing.getImageUrl())
                    .build());
        }

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, playingResponses, "게시글 목록을 성공적으로 불러왔습니다."));

    }

}