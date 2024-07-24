package com.likelion.RePlay.playing.dto;

import com.likelion.RePlay.enums.Category;
import com.likelion.RePlay.enums.District;
import com.likelion.RePlay.enums.State;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayingFilteringDTO {
    private List<Date> dateList;
    private List<State> stateList;
    private List<District> districtList;
    private Category category;
}
