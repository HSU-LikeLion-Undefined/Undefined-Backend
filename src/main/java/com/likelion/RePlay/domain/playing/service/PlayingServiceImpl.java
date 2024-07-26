package com.likelion.RePlay.domain.playing.service;

import com.likelion.RePlay.domain.playing.web.dto.PlayingFilteringDTO;
import com.likelion.RePlay.domain.playing.web.dto.PlayingListDTO;
import com.likelion.RePlay.domain.playing.web.dto.PlayingWriteRequestDTO;
import com.likelion.RePlay.domain.playing.entity.Playing;
import com.likelion.RePlay.domain.playing.entity.PlayingApply;
import com.likelion.RePlay.domain.playing.entity.QPlaying;
import com.likelion.RePlay.domain.playing.repository.PlayingApplyRepository;
import com.likelion.RePlay.domain.playing.repository.PlayingRepository;
import com.likelion.RePlay.domain.user.entity.User;
import com.likelion.RePlay.domain.user.repository.UserRepository;
import com.likelion.RePlay.global.enums.IsCompleted;
import com.likelion.RePlay.global.enums.IsRecruit;
import com.likelion.RePlay.global.response.CustomAPIResponse;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
public class PlayingServiceImpl implements PlayingService {

    private final UserRepository userRepository;
    private final PlayingRepository playingRepository;
    private final PlayingApplyRepository playingApplyRepository;

    @Override
    public ResponseEntity<CustomAPIResponse<?>> writePost(PlayingWriteRequestDTO playingWriteRequestDTO) {

        // 게시글 작성자가 DB에 존재하는가?
        Optional<User> findUser = userRepository.findByPhoneId(playingWriteRequestDTO.getPhoneId());

        // 없다면 오류 반환
        if (findUser.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 사용자입니다."));
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
        User user = findUser.get();
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

        PlayingListDTO.PlayingResponse playingResponse = PlayingListDTO.PlayingResponse.builder()
                .nickname(findPlaying.get().getUser().getNickname())
                .introduce(findPlaying.get().getUser().getIntroduce())
                .category(findPlaying.get().getCategory())
                .title(findPlaying.get().getTitle())
                .date(findPlaying.get().getDate())
                .state(findPlaying.get().getState())
                .district(findPlaying.get().getDistrict())
                .totalCount(findPlaying.get().getTotalCount())
                .recruitmentCount(findPlaying.get().getRecruitmentCount())
                .content(findPlaying.get().getContent())
                .cost(findPlaying.get().getCost())
                .costDescription(findPlaying.get().getCostDescription())
                .build();

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, playingResponse, "특정 게시글을 성공적으로 불러왔습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> filtering(PlayingFilteringDTO playingFilteringDTO) {

        BooleanBuilder builder = new BooleanBuilder();

        if (playingFilteringDTO.getCategory() != null) {
            builder.and(QPlaying.playing.category.eq(playingFilteringDTO.getCategory()));
        }
        if (playingFilteringDTO.getDateList() != null && !playingFilteringDTO.getDateList().isEmpty()) {
            BooleanBuilder dateBuilder = new BooleanBuilder();
            for (Date date : playingFilteringDTO.getDateList()) {
                dateBuilder.or(QPlaying.playing.date.eq(date));
            }
            builder.and(dateBuilder);
        }
        if (playingFilteringDTO.getStateList() != null && !playingFilteringDTO.getStateList().isEmpty()) {
            builder.and(QPlaying.playing.state.in(playingFilteringDTO.getStateList()));
        }
        if (playingFilteringDTO.getDistrictList() != null && !playingFilteringDTO.getDistrictList().isEmpty()) {
            builder.and(QPlaying.playing.district.in(playingFilteringDTO.getDistrictList()));
        }

        List<Playing> results = (List<Playing>) playingRepository.findAll(builder);

        // 메인 페이지에서 보여질 정보만 추출해서 보내기
        List<PlayingListDTO.PlayingResponse> playingResponse = new ArrayList<>();
        for (Playing result : results) {
            PlayingListDTO.PlayingResponse response = PlayingListDTO.PlayingResponse.builder()
                    .category(result.getCategory())
                    .title(result.getTitle())
                    .state(result.getState())
                    .district(result.getDistrict())
                    .date(result.getDate())
                    .totalCount(result.getTotalCount())
                    .recruitmentCount(result.getRecruitmentCount())
                    .build();
            playingResponse.add(response);
        }

        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, playingResponse, "조건에 맞는 게시글들을 성공적으로 불러왔습니다."));
    }

    @Override
    public ResponseEntity<CustomAPIResponse<?>> recruitPlaying(Long playingId, String phoneId) {

        // 놀이터 게시글이 DB에 존재하는가?
        Optional<Playing> findPlaying = playingRepository.findById(playingId);
        Optional<User> findUser = userRepository.findByPhoneId(phoneId);
        Optional<PlayingApply> findPlayingApply = playingApplyRepository.findByUserPhoneId(phoneId);

        // 존재하지 않는다면 오류 반환
        if (findPlaying.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomAPIResponse.createFailWithout(404, "존재하지 않는 게시글입니다."));
        }

        // 인원이 다 차거나 모집 완료된 활동일 경우 오류 반환
        if (findPlaying.get().getIsRecruit() == IsRecruit.FALSE) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "인원이 마감되었습니다."));
        } else if (findPlaying.get().getIsCompleted() == IsCompleted.TRUE) {
            return ResponseEntity.status(400)
                    .body(CustomAPIResponse.createFailWithout(400, "모집 완료된 활동입니다."));
        }

        // 이미 신청한 활동일 경우, 오류 반환
        for (PlayingApply apply : findUser.get().getPlayingApplies()) {
            if (apply.getPlayingApplyId().equals(findPlayingApply.get().getPlayingApplyId())) {
                return ResponseEntity.status(400)
                        .body(CustomAPIResponse.createFailWithout(400, "이미 신청한 활동입니다."));
            }
        }

        // 신청하지 않은 활동일 경우
        // 해당 게시글 신청 정보에 해당 유저의 정보를 추가한다.
        findPlayingApply.get().changeUser(findUser.get());
        findPlayingApply.get().changePlaying(findPlaying.get());

        // 해당 게시글에 모집 인원을 추가한다.
        findPlaying.get().changeRecruitmentCount(findPlaying.get().getRecruitmentCount() + 1);

        // 모집인원이 다 찼을 경우, 모집중 -> 모집완료로 바꾼다.
        if (findPlaying.get().getRecruitmentCount() == findPlaying.get().getTotalCount()) {
            findPlaying.get().changeIsRecruit(IsRecruit.FALSE);
        }

        PlayingListDTO.PlayingResponse playingResponse = PlayingListDTO.PlayingResponse.builder()
                .category(findPlaying.get().getCategory())
                .title(findPlaying.get().getTitle())
                .date(findPlaying.get().getDate())
                .cost(findPlaying.get().getCost())
                .build();
        return ResponseEntity.status(200)
                .body(CustomAPIResponse.createSuccess(200, playingResponse, "활동 신청이 성공적으로 완료되었습니다."));
    }
}