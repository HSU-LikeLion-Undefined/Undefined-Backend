package com.likelion.RePlay.domain.learning.web.dto;


import com.likelion.RePlay.global.enums.Category;
import com.likelion.RePlay.global.enums.District;
import com.likelion.RePlay.global.enums.State;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningFilteringDTO {
    private List<String> dateList;
    private List<State> stateList;
    private List<District> districtList;
    private Category category;
}
