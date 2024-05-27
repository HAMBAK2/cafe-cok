package com.sideproject.cafe_cok.plan.domain;


import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.plan.domain.enums.MatchType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.aop.target.LazyInitTargetSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Table(name = "plans")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plan extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "members_id")
    private Member member;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "visit_date")
    private LocalDate visitDate;

    @Column(name = "visit_start_time")
    private LocalTime visitStartTime;

    @Column(name = "visit_end_time")
    private LocalTime visitEndTime;

    @Column(name = "minutes")
    private Integer minutes;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "match_type", nullable = false)
    private MatchType matchType;

    @Column(name = "is_saved", nullable = false)
    private Boolean isSaved;

    @Column(name = "is_shared", nullable = false)
    private Boolean isShared;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<PlanCafe> planCafes = new ArrayList<>();

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<PlanKeyword> planKeywords = new ArrayList<>();

    public Plan(final Member member,
                final String locationName,
                final LocalDate visitDate,
                final LocalTime visitStartTime,
                final LocalTime visitEndTime,
                final Integer minutes,
                final MatchType matchType,
                final Boolean isSaved,
                final Boolean isShared) {

        this.member = member;
        this.locationName = locationName;
        this.visitDate = visitDate;
        this.visitStartTime = visitStartTime;
        this.visitEndTime = visitEndTime;
        this.minutes = minutes;
        this.matchType = matchType;
        this.isSaved = isSaved;
        this.isShared = isShared;
    }

    public void changeIsSaved(final Boolean isSaved) {
        this.isSaved = isSaved;
    }

    public void changeIsShared(final Boolean isShared) {
        this.isShared = isShared;
    }

    public void changeMember(final Member member) { this.member = member; }
}
