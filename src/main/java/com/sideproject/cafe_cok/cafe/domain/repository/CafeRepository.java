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
                .orElseThrow(() ->
                        new NoSuchCafeException("[ID : " + id + "] 에 해당하는 카페가 존재하지 않습니다."));
    }

    List<Cafe> findAllByOrderByIdDesc();

    boolean existsByKakaoId(final Long kakaoId);
}
