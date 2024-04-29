package com.sideproject.hororok.keword.application;

import com.sideproject.hororok.keword.domain.CafeReviewKeyword;
import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.repository.KeywordRepository;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.keword.dto.KeywordCount;
import com.sideproject.hororok.keword.dto.KeywordDto;
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


    public List<KeywordCount> getUserChoiceKeywordCounts(Long cafeId) {
        List<KeywordCount> allCafeKeywordCounts
                = keywordRepository.findKeywordCountsByCafeId(cafeId);

        if (allCafeKeywordCounts != null && allCafeKeywordCounts.size() > USER_CHOICE_KEYWORD_CNT) {
            allCafeKeywordCounts = allCafeKeywordCounts.subList(0, USER_CHOICE_KEYWORD_CNT);
        }

        return allCafeKeywordCounts;
    }

    public CategoryKeywordsDto getAllCategoryKeywords() {
        List<Keyword> keywords = keywordRepository.findAll();
        return new CategoryKeywordsDto(keywords);
    }

    public CategoryKeywordsDto getCategoryKeywords(final List<String> keywordNames) {

        List<Keyword> keywords = keywordRepository.findByNameIn(keywordNames);
        return new CategoryKeywordsDto(keywords);
    }


    public CategoryKeywordsDto getCategoryKeywords(final Long reviewId) {

        List<Keyword> keywords = keywordRepository.findByReviewId(reviewId);
        return new CategoryKeywordsDto(keywords);
    }

    public List<KeywordDto> getKeywordDtosByReviewId(final Long reviewId) {
        List<Keyword> keywords = keywordRepository.findByReviewId(reviewId);
        return KeywordDto.fromList(keywords);
    }



}
