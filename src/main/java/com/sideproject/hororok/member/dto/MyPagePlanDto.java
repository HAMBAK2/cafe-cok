package com.sideproject.hororok.member.dto;

import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.plan.domain.Plan;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import static com.sideproject.hororok.utils.FormatConverter.convertLocalDateLocalTimeToString;


@Getter
@Builder
public class MyPagePlanDto {

    @NotNull
    private final Long id;
    @NotNull
    private final String keywordName;
    private final String location;
    private final String visitDateTime;

    public static MyPagePlanDto of(final Plan plan, final String keywordName) {
        return MyPagePlanDto.builder()
                .id(plan.getId())
                .keywordName(keywordName)
                .location(plan.getLocationName())
                .visitDateTime(convertLocalDateLocalTimeToString(plan.getVisitDate(), plan.getVisitStartTime()))
                .build();
    }

}