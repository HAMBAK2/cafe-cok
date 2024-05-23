package com.sideproject.cafe_cok.combination.application;

import static com.sideproject.cafe_cok.common.fixtures.CombinationFixtures.*;
import static com.sideproject.cafe_cok.common.fixtures.KeywordFixtures.키워드_리스트;
import static com.sideproject.cafe_cok.common.fixtures.KeywordFixtures.키워드_이름_리스트;
import static com.sideproject.cafe_cok.common.fixtures.LoginMemberFixtures.로그인_맴버;
import static com.sideproject.cafe_cok.common.fixtures.MemberFixtures.사용자;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.combination.application.CombinationService;
import com.sideproject.cafe_cok.combination.domain.Combination;
import com.sideproject.cafe_cok.combination.domain.repository.CombinationKeywordRepository;
import com.sideproject.cafe_cok.combination.domain.repository.CombinationRepository;
import com.sideproject.cafe_cok.combination.dto.request.CombinationRequest;
import com.sideproject.cafe_cok.combination.dto.response.CombinationDetailResponse;
import com.sideproject.cafe_cok.combination.dto.response.CombinationIdResponse;
import com.sideproject.cafe_cok.common.annotation.ServiceTest;
import com.sideproject.cafe_cok.keword.domain.repository.KeywordRepository;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class CombinationServiceTest extends ServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private  KeywordRepository keywordRepository;

    @Mock
    private CombinationRepository combinationRepository;

    @Mock
    private CombinationKeywordRepository combinationKeywordRepository;

    @InjectMocks
    private CombinationService combinationService;

    @Test
    @DisplayName("내 조합 생성 - 성공")
    public void test_create() {

        //given
        CombinationRequest request = 조합_생성_수정_요청();
        CombinationIdResponse response = 조합_생성_수정_응답();
        LoginMember loginMember = 로그인_맴버();

        given(memberRepository.getById(any(Long.class))).willReturn(사용자());
        given(combinationRepository.save(any(Combination.class))).willReturn(조합());

        //when
        CombinationIdResponse actual = combinationService.create(request, loginMember);

        //then
        assertThat(actual.getCombinationId()).isEqualTo(response.getCombinationId());
    }

    @Test
    @DisplayName("내 조합 조회 - 성공")
    public void test_detail() {

        Long combinationId = 조합_ID;
        CombinationDetailResponse response = 조합_조회_응답();
        given(combinationRepository.getById(any(Long.class))).willReturn(조합());
        given(keywordRepository.findByCombinationId(any(Long.class))).willReturn(키워드_리스트);

        CombinationDetailResponse actual = combinationService.detail(combinationId);

        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(response.getId());
            assertThat(actual.getName()).isEqualTo(response.getName());
            assertThat(actual.getIcon()).isEqualTo(response.getIcon());
            assertThat(actual.getCategoryKeywords()).isEqualTo(response.getCategoryKeywords());
        });
    }

    @Test
    @DisplayName("내 조합 수정 - 키워드가 다른 경우")
    public void test_edit_not_same_keywords() {

        Long combinationId = 조합_ID;
        CombinationRequest request = 조합_생성_수정_요청();
        CombinationIdResponse response = 조합_생성_수정_응답();
        Combination combination = 조합();

        given(combinationRepository.getById(any(Long.class))).willReturn(combination);

        CombinationIdResponse actual = combinationService.edit(request, combinationId);
        verify(combinationKeywordRepository).deleteByCombinationId(combinationId);
        assertThat(actual.getCombinationId()).isEqualTo(response.getCombinationId());
    }

    @Test
    @DisplayName("내 조합 수정 - 키워드가 같은 경우")
    public void test_edit_same_keywords() {

        Long combinationId = 조합_ID;
        CombinationRequest request = 조합_생성_수정_요청();
        CombinationIdResponse response = 조합_생성_수정_응답();
        List<String> keywordNames = 키워드_이름_리스트;
        Combination combination = 조합();

        given(combinationRepository.getById(any(Long.class))).willReturn(combination);
        given(keywordRepository.findNamesByCombinationId(any(Long.class))).willReturn(keywordNames);

        CombinationIdResponse actual = combinationService.edit(request, combinationId);
        verify(combinationKeywordRepository, never()).deleteByCombinationId(combinationId);
        assertThat(actual.getCombinationId()).isEqualTo(response.getCombinationId());
    }
}