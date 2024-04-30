package com.sideproject.hororok.keword.domain.repository;

import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.enums.Category;
import com.sideproject.hororok.keword.dto.KeywordCountDto;
import com.sideproject.hororok.keword.exception.NoSuchKeywordException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Optional<Keyword> findByName(String name);

    default Keyword getByName(String name) {
        return findByName(name)
                .orElseThrow(NoSuchKeywordException::new);
    }

    @Query("SELECT k " +
            "FROM Keyword k " +
                "JOIN CafeReviewKeyword crk ON k.id = crk.keyword.id " +
            "WHERE crk.review.id = :reviewId " +
                "AND k.category = :category")
    List<Keyword> findByReviewIdAndCategory(final Long reviewId, final Category category);

    @Query("SELECT k " +
            "FROM Keyword k " +
            "JOIN CafeReviewKeyword crk ON k.id = crk.keyword.id " +
            "WHERE crk.review.id = :reviewId " +
            "AND k.category = :category")
    List<Keyword> findByReviewIdAndCategory(final Long reviewId, final Category category, final Pageable pageable);

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


    @Query("SELECT NEW com.sideproject.hororok.keword.dto.KeywordCountDto(k.id, k.name, COUNT(crk)) " +
            "FROM Keyword k " +
            "JOIN k.cafeReviewKeywords crk " +
            "WHERE crk.cafe.id = :cafeId " +
            "GROUP BY k.id " +
            "ORDER BY COUNT(crk) DESC")
    List<KeywordCountDto> findKeywordCountsByCafeId(@Param("cafeId") Long cafeId);

    @Query("SELECT k " +
            "FROM Keyword k " +
            "JOIN k.cafeReviewKeywords crk " +
            "WHERE crk.cafe.id = :cafeId " +
            "GROUP BY k.id " +
            "ORDER BY COUNT(crk) DESC")
    List<Keyword> findKeywordsByCafeIdOrderByCountDesc(@Param("cafeId") Long cafeId, Pageable pageable);

    @Query("SELECT k FROM Keyword k " +
            "JOIN k.cafeReviewKeywords crk " +
            "WHERE crk.cafe.id = :cafeId")
    List<Keyword> findByCafeId(@Param("cafeId") Long cafeId);


    List<Keyword> findByNameIn(List<String> keywordNames);

    @Query("SELECT k " +
            "FROM Keyword k " +
                "JOIN CombinationKeyword ck ON k.id = ck.keyword.id " +
            "WHERE ck.combination.id = :combinationId")
    List<Keyword> findByCombinationId(final Long combinationId);

    @Query("SELECT k.name " +
            "FROM Keyword k " +
            "JOIN CombinationKeyword ck ON k.id = ck.keyword.id " +
            "WHERE ck.combination.id = :combinationId")
    List<String> findNamesByCombinationId(final Long combinationId);
}
