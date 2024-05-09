package com.sideproject.cafe_cok.combination.domain.repository;

import com.sideproject.cafe_cok.combination.domain.Combination;
import com.sideproject.cafe_cok.combination.exception.NoSuchCombinationException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CombinationRepository extends JpaRepository<Combination, Long> {

    List<Combination> findByMemberId(final Long memberId);

    default Combination getById(final Long combinationId) {
        return findById(combinationId)
                .orElseThrow(NoSuchCombinationException::new);
    }
}
