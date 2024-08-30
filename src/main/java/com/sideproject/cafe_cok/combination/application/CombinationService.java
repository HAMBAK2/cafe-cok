package com.sideproject.cafe_cok.combination.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.combination.domain.repository.CombinationKeywordRepository;
import com.sideproject.cafe_cok.combination.domain.repository.CombinationRepository;
import com.sideproject.cafe_cok.combination.dto.CombinationDto;
import com.sideproject.cafe_cok.keword.domain.repository.KeywordRepository;
import com.sideproject.cafe_cok.keword.dto.CategoryKeywordsDto;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.combination.dto.response.CombinationListResponse;
import com.sideproject.cafe_cok.utils.ListUtils;
import com.sideproject.cafe_cok.combination.domain.Combination;
import com.sideproject.cafe_cok.combination.domain.CombinationKeyword;
import com.sideproject.cafe_cok.combination.dto.request.CombinationRequest;
import com.sideproject.cafe_cok.combination.dto.response.CombinationIdResponse;
import com.sideproject.cafe_cok.combination.dto.response.CombinationResponse;
import com.sideproject.cafe_cok.keword.domain.Keyword;
import com.sideproject.cafe_cok.member.domain.Member;
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


    public CombinationResponse detail(final Long combinationId) {

        Combination findCombination = combinationRepository.getById(combinationId);
        List<Keyword> findKeywords = keywordRepository.findByCombinationId(combinationId);
        CategoryKeywordsDto categoryKeywords = new CategoryKeywordsDto(findKeywords);
        return CombinationResponse.of(findCombination, categoryKeywords);
    }

    @Transactional
    public CombinationIdResponse edit(final CombinationRequest request, final Long  combinationId) {

        Combination findCombination = combinationRepository.getById(combinationId);
        findCombination.changeByRequest(request);

        List<String> findKeywords = keywordRepository.findNamesByCombinationId(combinationId);
        if(ListUtils.areListEqual(request.getKeywords(), findKeywords))
            return CombinationIdResponse.of(findCombination.getId());

        combinationKeywordRepository.deleteById(combinationId);
        saveCombinationKeywordsByCombinationAndKeywordNames(findCombination, request.getKeywords());
        return CombinationIdResponse.of(findCombination.getId());
    }

    public CombinationListResponse combination(final LoginMember loginMember) {

        List<CombinationDto> findCombinations = combinationRepository.findByMemberId(loginMember.getId());
        if(findCombinations.isEmpty()) return new CombinationListResponse();
        return new CombinationListResponse(findCombinations);
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

