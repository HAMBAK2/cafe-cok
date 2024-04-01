package com.sideproject.hororok.cafe.repository;

import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.keword.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.print.DocFlavor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    @LogTrace
    Optional<Cafe> findById(Long id);

    @LogTrace
    List<Cafe> findAll();

    @LogTrace
    boolean existsByLongitudeAndLatitude(BigDecimal longitude, BigDecimal latitude);

    @LogTrace
    Optional<Cafe> findByLongitudeAndLatitude(BigDecimal longitude, BigDecimal latitude);

    @LogTrace
    List<Cafe> findAllByOrderByStarRatingDescNameAsc();

    @LogTrace
    @Query("SELECT DISTINCT c FROM Cafe c " +
            "JOIN c.reviews r " +
            "JOIN r.keywords k " +
            "WHERE k.name IN :keywords " +
            "GROUP BY c.id " +
            "HAVING COUNT(DISTINCT k.id) = :keywordCount")
    List<Cafe> findAllByReviewAllInKeywords(List<String> keywords);

    @LogTrace
    @Query("SELECT c FROM Cafe c " +
            "JOIN c.reviews r " +
            "JOIN r.keywords k " +
            "WHERE k.name IN :keywords")
    List<Cafe> findAllByReviewSomeInKeywords(List<String> keywords);

    @LogTrace
    @Query("SELECT DISTINCT c " +
            "FROM Cafe c " +
            "JOIN c.reviews r " +
            "JOIN r.keywords k " +
            "WHERE " +
            "c.id = :cafeId " +
            "AND k.name IN :keywords")
    Optional<Cafe> findDistinctByKeywordsAndCafeId(List<String> keywords, Long cafeId);

    @LogTrace
    @Query("SELECT DISTINCT k.name " +
            "FROM Cafe c " +
                "JOIN c.reviews r " +
                "JOIN r.keywords k " +
            "WHERE c.id = :cafeId")
    List<String> findKeywordsByReviewsCafeId(Long cafeId);

}
