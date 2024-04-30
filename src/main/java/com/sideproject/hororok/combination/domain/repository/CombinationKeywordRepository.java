package com.sideproject.hororok.combination.domain.repository;

import com.sideproject.hororok.combination.domain.CombinationKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CombinationKeywordRepository extends JpaRepository<CombinationKeyword, Long> {


    void deleteByCombinationId(final Long combinationId);

}
