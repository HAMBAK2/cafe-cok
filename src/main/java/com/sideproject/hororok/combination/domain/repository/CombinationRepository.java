package com.sideproject.hororok.combination.domain.repository;

import com.sideproject.hororok.combination.domain.Combination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CombinationRepository extends JpaRepository<Combination, Long> {

    List<Combination> findByMemberId(final Long memberId);
}
