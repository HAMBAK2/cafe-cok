package com.sideproject.cafe_cok.keword.domain.repository;

import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.keword.exception.NoSuchKeywordException;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.keword.dto.KeywordCountDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long>, KeywordRepositoryCustom {

    List<Keyword> findByNameIn(final List<String> keywordNames);

    Optional<Keyword> findByName(final String keywordName);

    default Keyword getByName(final String keywordName){
        return findByName(keywordName)
                .orElseThrow(NoSuchKeywordException::new);
    }

    @Query("SELECT k.name " +
            "FROM Keyword k " +
            "JOIN CafeReviewKeyword crk ON k.id = crk.keyword.id " +
            "WHERE crk.review.id = :reviewId " +
            "AND k.category = :category")
    List<String> findNameByReviewIdAndCategory(final Long reviewId, final Category category, final Pageable pageable);

    @Query("SELECT k " +
            "FROM Keyword k " +
            "JOIN CafeReviewKeyword crk ON k.id = crk.keyword.id " +
            "WHERE crk.review.id = :reviewId")
    List<Keyword> findByReviewId(final Long reviewId);

    @Query("SELECT k.name " +
            "FROM Keyword k " +
            "JOIN CafeReviewKeyword crk ON k.id = crk.keyword.id " +
            "WHERE crk.review.id = :reviewId")
    List<String> findNamesByReviewId(final Long reviewId);


    @Query("SELECT NEW com.sideproject.cafe_cok.keword.dto.KeywordCountDto(k.name, COUNT(crk)) " +
            "FROM Keyword k " +
                "JOIN CafeReviewKeyword crk ON k.id = crk.keyword.id " +
            "WHERE crk.cafe.id = :cafeId " +
            "GROUP BY k.id " +
            "ORDER BY COUNT(crk) DESC")
    List<KeywordCountDto> findKeywordCountsByCafeId(@Param("cafeId") Long cafeId);

    @Query("SELECT k " +
            "FROM Keyword k " +
                "JOIN CafeReviewKeyword crk ON k.id = crk.keyword.id " +
            "WHERE crk.cafe.id = :cafeId " +
            "GROUP BY k.id " +
            "ORDER BY COUNT(crk) DESC")
    List<Keyword> findKeywordsByCafeIdOrderByCountDesc(@Param("cafeId") Long cafeId, Pageable pageable);

    @Query("SELECT k FROM Keyword k " +
                "JOIN CafeReviewKeyword crk ON k.id = crk.keyword.id " +
            "WHERE crk.cafe.id = :cafeId")
    List<Keyword> findByCafeId(@Param("cafeId") Long cafeId);

    @Query("SELECT k " +
            "FROM Keyword k " +
            "JOIN PlanKeyword pk ON k.id = pk.keyword.id " +
            "WHERE pk.id = :planId")
    List<Keyword> findKeywordByPlanId(final Long planId);
}
