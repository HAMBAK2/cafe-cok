package com.sideproject.cafe_cok.keword.domain.repository;

import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.keword.domain.enums.Category;

import java.util.List;

public interface KeywordRepositoryCustom {

    List<Keyword> findByCombinationId(final Long combinationId);

    List<String> findNamesByCombinationId(final Long combinationId);

    List<Keyword> findByReviewIdAndCategory(final Long reviewId, final Category category);
}
