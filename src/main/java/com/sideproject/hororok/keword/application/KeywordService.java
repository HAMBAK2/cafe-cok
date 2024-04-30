package com.sideproject.hororok.keword.application;

import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.repository.KeywordRepository;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public CategoryKeywordsDto getCategoryKeywords(final List<String> keywordNames) {

        List<Keyword> keywords = keywordRepository.findByNameIn(keywordNames);
        return new CategoryKeywordsDto(keywords);
    }


    public CategoryKeywordsDto getCategoryKeywords(final Long reviewId) {

        List<Keyword> keywords = keywordRepository.findByReviewId(reviewId);
        return new CategoryKeywordsDto(keywords);
    }

}
