package com.sideproject.cafe_cok.cafe.dto;

import com.sideproject.cafe_cok.cafe.domain.OperationHour;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "카페 운영 시간 DTO")
public class CafeOperationHourDto {

    @Schema(description = "요일", example = "월")
    private String day;

    @Schema(description = "시작 시간(시간)", example = "10")
    private Integer startHour = 0;

    @Schema(description = "시작 시간(분)", example = "30")
    private Integer startMinute = 0;

    @Schema(description = "종료 시간(시간)", example = "23")
    private Integer endHour = 0;

    @Schema(description = "종료 시간(분)", example = "40")
    private Integer endMinute = 0;

    @Builder
    public CafeOperationHourDto(final String day,
                                final Integer startHour,
                                final Integer startMinute,
                                final Integer endHour,
                                final Integer endMinute) {
        this.day = day;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    public void changeTime(OperationHour operationHour) {
        this.startHour = operationHour.getOpeningTime().getHour();
        this.startMinute = operationHour.getOpeningTime().getMinute();
        this.endHour = operationHour.getClosingTime().getHour();
        this.endMinute = operationHour.getClosingTime().getMinute();
    }

}
