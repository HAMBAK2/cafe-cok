package com.sideproject.cafe_cok.cafe.domain.repository;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.exception.NoSuchCafeException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long>, CafeRepositoryCustom {

    default Cafe getById(final Long id) {
        return findById(id)
                .orElseThrow(NoSuchCafeException::new);
    }

    List<Cafe> findAllByOrderByStarRatingDescNameAsc();
}
