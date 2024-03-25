package com.sideproject.hororok.cafe.repository;

import com.sideproject.hororok.cafe.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    Optional<Cafe> findById(Long id);

}
