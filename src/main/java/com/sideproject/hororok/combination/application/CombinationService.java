package com.sideproject.hororok.combination.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.combination.domain.Combination;
import com.sideproject.hororok.combination.domain.CombinationKeyword;
import com.sideproject.hororok.combination.domain.repository.CombinationKeywordRepository;
import com.sideproject.hororok.combination.domain.repository.CombinationRepository;
import com.sideproject.hororok.combination.dto.request.CombinationRequest;
import com.sideproject.hororok.combination.dto.response.CombinationIdResponse;
import com.sideproject.hororok.combination.dto.response.CombinationDetailResponse;
import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.repository.KeywordRepository;
import com.sideproject.hororok.keword.dto.CategoryKeywordsDto;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.repository.MemberRepository;
import com.sideproject.hororok.utils.ListUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CombinationService {

    private final MemberRepository memberRepository;
    private final KeywordRepository keywordRepository;
    private final CombinationRepository combinationRepository;
    private final CombinationKeywordRepository combinationKeywordRepository;

    @Transactional
    public CombinationIdResponse create(final CombinationRequest request, final LoginMember loginMember) {

        Member findMember = memberRepository.getById(loginMember.getId());
        Combination combination = request.toEntity(findMember);
        Combination savedCombination = combinationRepository.save(combination);

        saveCombinationKeywordsByCombinationAndKeywordNames(savedCombination, request.getKeywords());
        return CombinationIdResponse.of(savedCombination.getId());
    }


    public CombinationDetailResponse detail(final Long combinationId) {

        Combination findCombination = combinationRepository.getById(combinationId);
        List<Keyword> findKeywords = keywordRepository.findByCombinationId(combinationId);
        CategoryKeywordsDto categoryKeywords = new CategoryKeywordsDto(findKeywords);

        return CombinationDetailResponse.of(findCombination, categoryKeywords);
    }

    @Transactional
    public CombinationIdResponse edit(final CombinationRequest request, final Long  combinationId) {

        Combination findCombination = combinationRepository.getById(combinationId);
        findCombination.setName(request.getName());
        findCombination.setIcon(request.getIcon());

        Combination savedCombination = combinationRepository.save(findCombination);
        List<String> findKeywords = keywordRepository.findNamesByCombinationId(combinationId);
        if(ListUtils.areListEqual(request.getKeywords(), findKeywords))
            return CombinationIdResponse.of(savedCombination.getId());

        combinationKeywordRepository.deleteByCombinationId(combinationId);
        saveCombinationKeywordsByCombinationAndKeywordNames(savedCombination, request.getKeywords());

        return CombinationIdResponse.of(savedCombination.getId());
    }

    private void saveCombinationKeywordsByCombinationAndKeywordNames(
            final Combination combination, final List<String> keywordNames) {

        List<Keyword> findKeywords = keywordRepository.findByNameIn(keywordNames);
        List<CombinationKeyword> combinationKeywords = findKeywords.stream()
                .map(keyword -> new CombinationKeyword(combination, keyword))
                .collect(Collectors.toList());

        combinationKeywordRepository.saveAll(combinationKeywords);
    }
}

