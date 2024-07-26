package com.likelion.RePlay.domain.playing.web.dto;


import com.likelion.RePlay.global.enums.Category;
import com.likelion.RePlay.global.enums.District;
import com.likelion.RePlay.global.enums.State;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayingFilteringDTO {
    private List<String> dateList;
    private List<State> stateList;
    private List<District> districtList;
    private Category category;
}
