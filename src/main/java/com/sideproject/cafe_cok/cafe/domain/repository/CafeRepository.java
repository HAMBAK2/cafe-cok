package com.sideproject.cafe_cok.cafe.domain.repository;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.exception.NoSuchCafeException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CafeRepository extends JpaRepository<Cafe, Long>, CafeRepositoryCustom {

    default Cafe getById(final Long id) {
        return findById(id)
                .orElseThrow(NoSuchCafeException::new);
    }

    Optional<Cafe> findByLatitudeAndLongitude(final BigDecimal latitude,
                                              final BigDecimal longitude);

    List<Cafe> findAllByOrderByIdDesc();

    boolean existsByKakaoId(Long kakaoId);


}
