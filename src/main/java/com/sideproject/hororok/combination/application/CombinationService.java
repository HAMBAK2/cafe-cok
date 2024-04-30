package com.sideproject.hororok.combination.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.combination.domain.Combination;
import com.sideproject.hororok.combination.domain.CombinationKeyword;
import com.sideproject.hororok.combination.domain.repository.CombinationKeywordRepository;
import com.sideproject.hororok.combination.domain.repository.CombinationRepository;
import com.sideproject.hororok.combination.dto.request.CombinationCreateRequest;
import com.sideproject.hororok.combination.dto.response.CombinationCreateResponse;
import com.sideproject.hororok.keword.domain.Keyword;
import com.sideproject.hororok.keword.domain.repository.KeywordRepository;
import com.sideproject.hororok.member.domain.Member;
import com.sideproject.hororok.member.domain.repository.MemberRepository;
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
    private final CombinationRepository combinationRepository;
    private final KeywordRepository keywordRepository;
    private final CombinationKeywordRepository combinationKeywordRepository;

    @Transactional
    public CombinationCreateResponse create(final CombinationCreateRequest request, final LoginMember loginMember) {

        Member findMember = memberRepository.getById(loginMember.getId());
        Combination combination = request.toEntity(findMember);
        Combination savedCombination = combinationRepository.save(combination);

        List<Keyword> findKeywords = keywordRepository.findByNameIn(request.getKeywords());
        List<CombinationKeyword> combinationKeywords = findKeywords.stream()
                .map(keyword -> new CombinationKeyword(savedCombination, keyword))
                .collect(Collectors.toList());

        combinationKeywordRepository.saveAll(combinationKeywords);
        return CombinationCreateResponse.of(savedCombination.getId());
    }
}
