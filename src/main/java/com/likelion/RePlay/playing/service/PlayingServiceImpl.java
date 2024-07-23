package com.likelion.RePlay.playing.service;

import com.likelion.RePlay.entity.User;
import com.likelion.RePlay.entity.playing.Playing;
import com.likelion.RePlay.enums.IsCompleted;
import com.likelion.RePlay.enums.IsRecruit;
import com.likelion.RePlay.playing.dto.PlayingWriteRequestDTO;
import com.likelion.RePlay.playing.repository.PlayingRepository;
import com.likelion.RePlay.user.repository.UserRepository;
import com.likelion.RePlay.util.response.CustomAPIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class PlayingServiceImpl implements PlayingService{

    private final UserRepository userRepository;
    private final PlayingRepository playingRepository;

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
        Playing newPlaying = Playing.builder()
                .title(playingWriteRequestDTO.getTitle())
                .category(playingWriteRequestDTO.getCategory())
                .date(date)
                .isRecruit(IsRecruit.TRUE)
                .isCompleted(IsCompleted.FALSE)
                .totalCount(playingWriteRequestDTO.getTotalCount())
                .recruitCount(0L)
                .content(playingWriteRequestDTO.getContent())
                .cost(Long.valueOf(playingWriteRequestDTO.getCost()))
                .costDescription(playingWriteRequestDTO.getCostDescription())
                .build();

        playingRepository.save(newPlaying);


        return ResponseEntity.status(201)
                .body(CustomAPIResponse.createSuccess(201, null, "게시글을 성공적으로 작성하였습니다."));
    }
}