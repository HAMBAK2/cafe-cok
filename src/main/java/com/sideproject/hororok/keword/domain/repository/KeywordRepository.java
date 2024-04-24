package com.sideproject.hororok.keword.domain.repository;

import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.dto.KeywordCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Optional<Keyword> findByName(String name);


    @Query("SELECT NEW com.sideproject.hororok.keword.dto.KeywordCount(k.id, k.name, COUNT(crk)) " +
            "FROM Keyword k " +
            "JOIN k.cafeReviewKeywords crk " +
            "WHERE crk.cafe.id = :cafeId " +
            "GROUP BY k.id " +
            "ORDER BY COUNT(crk) DESC")
    List<KeywordCount> findKeywordCountsByCafeId(@Param("cafeId") Long cafeId);

    @Query("SELECT k FROM Keyword k " +
            "JOIN k.cafeReviewKeywords crk " +
            "WHERE crk.cafe.id = :cafeId ")
    List<Keyword> findByCafeId(@Param("cafeId") Long cafeId);

    List<Keyword> findByNameIn(List<String> keywordNames);


}
