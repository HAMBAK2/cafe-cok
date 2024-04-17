package com.sideproject.hororok.keword.application;

import com.sideproject.hororok.keword.domain.CafeReviewKeyword;
import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.KeywordRepository;
import com.sideproject.hororok.keword.dto.KeywordCount;
import com.sideproject.hororok.keword.dto.KeywordInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordService {

    private final KeywordRepository keywordRepository;

    private final Integer USER_CHOICE_KEYWORD_CNT = 6;

    public KeywordInfo getKeywordInfoByReviewKeyword(CafeReviewKeyword cafeReviewKeyword) {
        return KeywordInfo.from(cafeReviewKeyword.getKeyword());
    }

    public List<KeywordInfo> getKeywordInfosByCafeReviewKeywords(List<CafeReviewKeyword> cafeReviewKeywords) {
        List<KeywordInfo> keywordInfos = new ArrayList<>();
        for (CafeReviewKeyword cafeReviewKeyword : cafeReviewKeywords) {
            keywordInfos.add(getKeywordInfoByReviewKeyword(cafeReviewKeyword));
        }

        return keywordInfos;
    }

    public List<KeywordCount> getUserChoiceKeywordCounts(Long cafeId) {
        List<KeywordCount> allCafeKeywordCounts
                = keywordRepository.findKeywordCountsByCafeId(cafeId);

        if (allCafeKeywordCounts != null && allCafeKeywordCounts.size() > USER_CHOICE_KEYWORD_CNT) {
            allCafeKeywordCounts = allCafeKeywordCounts.subList(0, USER_CHOICE_KEYWORD_CNT);
        }

        return allCafeKeywordCounts;
    }

    public List<Keyword> findByCafeId(Long cafeId) {
        return keywordRepository.findByCafeId(cafeId);
    }

}
