package com.sideproject.hororok.cafe.domain.repository;

import com.sideproject.hororok.cafe.domain.Cafe;
import com.sideproject.hororok.cafe.exception.NoSuchCafeException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    

    default Cafe getById(final Long id) {
        return findById(id)
                .orElseThrow(NoSuchCafeException::new);
    }


    
    Optional<Cafe> findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);

    
    List<Cafe> findAllByOrderByStarRatingDescNameAsc();

    List<Cafe> findByIdIn(List<Long> ids);
}
