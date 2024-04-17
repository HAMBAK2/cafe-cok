package com.sideproject.hororok.cafe.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    
    Optional<Cafe> findById(Long id);

    
    List<Cafe> findAll();

    
    Optional<Cafe> findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);

    
    List<Cafe> findAllByOrderByStarRatingDescNameAsc();
}
