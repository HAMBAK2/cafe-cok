package com.sideproject.cafe_cok.combination.domain.repository;

import com.sideproject.cafe_cok.combination.domain.Combination;
import com.sideproject.cafe_cok.combination.exception.NoSuchCombinationException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CombinationRepository extends JpaRepository<Combination, Long>, CombinationRepositoryCustom{

    default Combination getById(final Long combinationId) {
        return findById(combinationId)
                .orElseThrow(() ->
                        new NoSuchCombinationException("[ID : " + combinationId + "] 에 해당하는 조합이 존재하지 않습니다."));
    }
}
