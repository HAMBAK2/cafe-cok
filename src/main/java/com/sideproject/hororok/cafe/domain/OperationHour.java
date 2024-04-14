package com.sideproject.hororok.cafe.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
public class OperationHour extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CAFE_ID")
    private Cafe cafe;

    @Enumerated(EnumType.STRING)
    private DayOfWeek date;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private boolean isClosed;






}
