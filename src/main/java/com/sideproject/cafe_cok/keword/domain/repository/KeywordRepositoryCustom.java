package com.sideproject.cafe_cok.keword.domain.repository;

import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.keword.dto.KeywordCountDto;
import com.sideproject.cafe_cok.keword.dto.KeywordDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KeywordRepositoryCustom {

    List<Keyword> findByCombinationId(final Long combinationId);

    List<String> findNamesByCombinationId(final Long combinationId);

    List<String> findNamesByReviewIdAndCategory(final Long reviewId,
                                                final Category category,
                                                final Pageable pageable);

    List<String> findNamesByCafeId(final Long cafeId);

    List<KeywordDto> findByReviewIdAndCategory(final Long reviewId,
                                               final Category category);

    List<KeywordDto> findKeywordDtoListByCafeIdOrderByCountDesc(final Long cafeId,
                                                                final Pageable pageable);

    List<KeywordCountDto> findKeywordCountDtoListByCafeIdOrderByCountDesc(final Long cafeId,
                                                                          final Pageable pageable);

    List<String> findKeywordNamesByCategory(final List<String> keywordNames,
                                            final Category category);
}
