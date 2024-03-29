package com.sideproject.hororok.operationHours.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sideproject.hororok.cafe.entity.Cafe;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
public class OperationHour {

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
