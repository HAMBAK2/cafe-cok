package com.sideproject.hororok.plan.domain;


import com.sideproject.hororok.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Table(name = "plans")
@Entity
public class Plan {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "members_id")
    private Member member;

    @Column(name = "departure_name")
    private String departureName;

    @Column(name = "departure_ latitude",
            precision = 17, scale = 14)
    private BigDecimal departureLatitude;

    @Column(name = "departure_ longitude",
            precision = 17, scale = 14)
    private BigDecimal departureLongitude;

    @Column(name = "visit_date")
    private LocalDate visitDate;

    @Column(name = "visit_start_time")
    private LocalTime visitStartTime;

    @Column(name = "visit_end_time")
    private LocalTime visitEndTime;

    @Column(name = "within_minutes")
    private Integer withinMinutes;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "plan_result", nullable = false)
    private PlanResult planResult;

    @Column(name = "is_saved", nullable = false)
    private Boolean isSaved;

    @Column(name = "is_shared", nullable = false)
    private Boolean isShared;


}
