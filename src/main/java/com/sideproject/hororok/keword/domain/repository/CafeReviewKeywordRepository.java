package com.sideproject.hororok.keword.domain.repository;

import com.sideproject.hororok.keword.domain.CafeReviewKeyword;
import com.sideproject.hororok.keword.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CafeReviewKeywordRepository extends JpaRepository<CafeReviewKeyword, Long> {


    List<CafeReviewKeyword> findByKeywordIn(List<Keyword> keywords);

    void deleteByReviewId(Long reviewId);

}
