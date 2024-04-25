package com.sideproject.hororok.plan.application;

import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.repository.KeywordRepository;
import com.sideproject.hororok.plan.domain.Plan;
import com.sideproject.hororok.plan.domain.PlanKeyword;
import com.sideproject.hororok.plan.domain.repository.PlanKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanKeywordService {

    private final PlanKeywordRepository planKeywordRepository;
    private final KeywordRepository keywordRepository;

    public void saveAll(Plan plan, List<String> keywords) {

        List<Keyword> findKeywords = keywordRepository.findByNameIn(keywords);
        List<PlanKeyword> planKeywords = findKeywords.stream()
                .map(keyword -> new PlanKeyword(plan, keyword))
                .collect(Collectors.toList());

        planKeywordRepository.saveAll(planKeywords);
    }

}
