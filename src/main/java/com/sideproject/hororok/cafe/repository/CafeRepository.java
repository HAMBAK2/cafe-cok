package com.sideproject.hororok.cafe.repository;

import com.sideproject.hororok.cafe.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    Optional<Cafe> findById(Long id);
    List<Cafe> findAll();

    boolean existsByLongitudeAndLatitude(BigDecimal longitude, BigDecimal latitude);

    Optional<Cafe> findByLongitudeAndLatitude(BigDecimal longitude, BigDecimal latitude);
}
