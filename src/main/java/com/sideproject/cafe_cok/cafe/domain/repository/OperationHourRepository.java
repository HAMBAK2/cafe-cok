package com.sideproject.cafe_cok.cafe.domain.repository;

import com.sideproject.cafe_cok.cafe.domain.OperationHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationHourRepository extends JpaRepository<OperationHour, Long> {

    List<OperationHour> findByCafeId(final Long cafeId);

    void deleteByCafeId(final Long cafeId);
}
