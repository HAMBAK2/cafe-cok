package com.sideproject.cafe_cok.plan.dto.request;

import com.sideproject.cafe_cok.cafe.condition.CafeSearchCondition;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.plan.domain.Plan;
import com.sideproject.cafe_cok.plan.domain.enums.MatchType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "계획 저장 요청")
public class PlanSaveRequest {

    @Schema(description = "계획 장소명", example = "망원역")
    private String locationName;

    @Schema(description = "계획 장소 위도", example = "37.57061772252790")
    private BigDecimal latitude;

    @Schema(description = "계획 장소 경도", example = "126.98055287409800")
    private BigDecimal longitude;

    @Schema(description = "계획한 도보거리", example = "30")
    private Integer minutes;

    @Schema(description = "계획한 방문일자", example = "2024-09-11")
    private LocalDate date;

    @Schema(description = "계획한 방문 시작 시간", example = "10:00")
    private LocalTime startTime;

    @Schema(description = "계획한 방문 종료 시간", example = "12:00")
    private LocalTime endTime;

    @Schema(description = "계획한 키워드 리스트", example = "데이트/모임")
    private List<String> keywords;

    public PlanSaveRequest(final String locationName,
                           final BigDecimal latitude,
                           final BigDecimal longitude,
                           final Integer minutes,
                           final LocalDate date,
                           final LocalTime startTime,
                           final LocalTime endTime,
                           final List<String> keywords) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.minutes = minutes;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.keywords = keywords;
    }

    public Plan toEntity(final Member member,
                         final MatchType matchType) {
        return Plan.builder()
                .member(member)
                .locationName(locationName)
                .visitDate(date)
                .visitStartTime(startTime)
                .visitEndTime(endTime)
                .minutes(minutes)
                .matchType(matchType)
                .isSaved(false)
                .isShared(false)
                .build();
    }

    public CafeSearchCondition toCondition() {
        return new CafeSearchCondition(this);
    }

    public void validate() {

        if(date == null) throw new IllegalArgumentException("방문 날짜는 필수 값 입니다.");
        if(startTime == null || startTime.equals(LocalTime.MIDNIGHT)) throw new IllegalArgumentException("방문 시간은 필수 값 입니다.");
        if(locationName != null && !locationName.isBlank() && minutes == null)
            throw new IllegalArgumentException("방문지를 선택했을 시 도보 거리는 필수 값 입니다.");
        if(minutes != null && minutes == 0) this.minutes = 30;
    }
}
