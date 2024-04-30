package com.sideproject.hororok.keword.application;

import com.sideproject.hororok.keword.domain.CafeReviewKeyword;
import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.repository.CafeReviewKeywordRepository;
import com.sideproject.hororok.keword.domain.repository.KeywordRepository;
import com.sideproject.hororok.review.domain.Review;
import com.sideproject.hororok.utils.ListUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeReviewKeywordService {

    private final KeywordRepository keywordRepository;
    private final CafeReviewKeywordRepository cafeReviewKeywordRepository;

    public void saveByReviewAndKeywordNames(final Review review, final List<String> keywordNames) {

        for (String keywordName : keywordNames) {
            Keyword findKeyword = keywordRepository.getByName(keywordName);
            CafeReviewKeyword cafeReviewKeyword = new CafeReviewKeyword(review.getCafe(), review, findKeyword);
            cafeReviewKeywordRepository.save(cafeReviewKeyword);
        }
    }





}
