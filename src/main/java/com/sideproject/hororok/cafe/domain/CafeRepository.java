package com.sideproject.hororok.cafe.domain;

import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.cafe.domain.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    @LogTrace
    Optional<Cafe> findById(Long id);

    @LogTrace
    List<Cafe> findAll();

    @LogTrace
    Optional<Cafe> findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);

    @LogTrace
    List<Cafe> findAllByOrderByStarRatingDescNameAsc();
}
