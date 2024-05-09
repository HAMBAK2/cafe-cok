package com.sideproject.cafe_cok.plan.domain;


import com.sideproject.cafe_cok.global.entity.BaseEntity;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.plan.domain.enums.MatchType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Table(name = "plans")
@Entity
@NoArgsConstructor
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

    public Plan(final Member member, final String locationName,
                final LocalDate visitDate, final LocalTime visitStartTime, final LocalTime visitEndTime,
                final Integer minutes,final MatchType matchType, final Boolean isSaved, final Boolean isShared) {
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

    public void setIsSaved(final Boolean isSaved) {
        this.isSaved = isSaved;
    }
    public void setIsShared(final Boolean isShared) {
        this.isShared = isShared;
    }

    public void setMember(final Member member) {
        this.member = member;
    }
}
