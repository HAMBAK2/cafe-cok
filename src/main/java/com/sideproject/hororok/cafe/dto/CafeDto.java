package com.sideproject.hororok.cafe.dto;

import com.sideproject.hororok.cafe.entity.Cafe;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CafeDto {

    private final List<Cafe> cafes;
}
