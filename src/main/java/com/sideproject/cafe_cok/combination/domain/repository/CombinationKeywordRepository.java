package com.sideproject.cafe_cok.combination.domain.repository;

import com.sideproject.cafe_cok.combination.domain.CombinationKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CombinationKeywordRepository extends JpaRepository<CombinationKeyword, Long> {


    void deleteByCombinationId(final Long combinationId);

}
