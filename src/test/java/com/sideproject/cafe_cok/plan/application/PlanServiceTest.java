package com.sideproject.cafe_cok.plan.application;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.plan.application.PlanService;
import com.sideproject.cafe_cok.common.annotation.ServiceTest;
import com.sideproject.cafe_cok.keword.domain.repository.KeywordRepository;
import com.sideproject.cafe_cok.member.domain.Member;
import com.sideproject.cafe_cok.member.domain.repository.MemberRepository;
import com.sideproject.cafe_cok.plan.domain.Plan;
import com.sideproject.cafe_cok.plan.domain.enums.PlanStatus;
import com.sideproject.cafe_cok.plan.domain.repository.PlanCafeRepository;
import com.sideproject.cafe_cok.plan.domain.repository.PlanKeywordRepository;
import com.sideproject.cafe_cok.plan.domain.repository.PlanRepository;
import com.sideproject.cafe_cok.plan.dto.request.CreatePlanRequest;
import com.sideproject.cafe_cok.plan.dto.request.SavePlanRequest;
import com.sideproject.cafe_cok.plan.dto.request.SharePlanRequest;
import com.sideproject.cafe_cok.plan.dto.response.DeletePlanResponse;
import com.sideproject.cafe_cok.plan.dto.response.SavePlanResponse;
import com.sideproject.cafe_cok.plan.dto.response.SharePlanResponse;
import com.sideproject.cafe_cok.plan.exception.NoSuchPlanKeywordException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;

import static com.sideproject.cafe_cok.common.fixtures.KeywordFixtures.*;
import static com.sideproject.cafe_cok.common.fixtures.LoginMemberFixtures.로그인_맴버;
import static com.sideproject.cafe_cok.common.fixtures.MemberFixtures.사용자;
import static com.sideproject.cafe_cok.common.fixtures.PlanFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlanServiceTest extends ServiceTest {


    @Mock
    private PlanRepository planRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private KeywordRepository keywordRepository;

    @Mock
    private PlanCafeRepository planCafeRepository;

    @Mock
    private PlanKeywordRepository planKeywordRepository;


    @InjectMocks
    private PlanService planService;

    @Test
    @DisplayName("계획(여정) 저장된 계획에 저장")
    public void test_plan_save_saved() {

        Member member = 사용자();
        Plan plan = 계획();
        SavePlanRequest request = 계획_저장_요청();
        SavePlanResponse response = 계획_저장_응답();
        LoginMember loginMember = 로그인_맴버();

        when(planRepository.getById(any(Long.class))).thenReturn(plan);
        when(memberRepository.getById(any(Long.class))).thenReturn(member);
        when(planRepository.save(any(Plan.class))).thenReturn(계획());

        SavePlanResponse actual = planService.save(request, loginMember);

        verify(planRepository).getById(loginMember.getId());
        verify(memberRepository).getById(loginMember.getId());
        verify(planRepository).save(plan);

        assertThat(actual.getPlanId()).isEqualTo(response.getPlanId());
    }

    @Test
    @DisplayName("계획(여정) 공유된 계획에 저장")
    public void test_plan_save_shared() {

        Member member = 사용자();
        Plan plan = 계획();
        SharePlanRequest request = 계획_공유_요청();
        SharePlanResponse response = 계획_공유_응답();
        LoginMember loginMember = 로그인_맴버();

        when(planRepository.getById(any(Long.class))).thenReturn(plan);
        when(memberRepository.getById(any(Long.class))).thenReturn(member);
        when(planRepository.save(any(Plan.class))).thenReturn(계획());

        SharePlanResponse actual = planService.share(request, loginMember);

        verify(planRepository).getById(plan.getId());
        verify(memberRepository).getById(loginMember.getId());
        verify(planRepository).save(plan);

        assertThat(actual.getPlanId()).isEqualTo(response.getPlanId());
    }

    @Test
    @DisplayName("저장된 계획(여정) 삭제")
    public void test_saved_plan_delete() {

        PlanStatus status = PlanStatus.SAVED;
        Plan plan = 계획();
        DeletePlanResponse response = 계획_삭제_응답();

        when(planRepository.getById(any(Long.class))).thenReturn(plan);
        when(planRepository.save(any(Plan.class))).thenReturn(계획());

        DeletePlanResponse actual = planService.delete(status, plan.getId());

        verify(planRepository).getById(plan.getId());
        verify(planRepository).save(plan);

        assertThat(actual.getPlanId()).isEqualTo(response.getPlanId());
    }

    @Test
    @DisplayName("저장된 계획(여정) 삭제")
    public void test_shared_plan_delete() {

        PlanStatus status = PlanStatus.SHARED;
        Plan plan = 계획();
        DeletePlanResponse response = 계획_삭제_응답();

        when(planRepository.getById(any(Long.class))).thenReturn(plan);
        when(planRepository.save(any(Plan.class))).thenReturn(plan);

        DeletePlanResponse actual = planService.delete(status, plan.getId());

        verify(planRepository).getById(plan.getId());
        verify(planRepository).save(plan);

        assertThat(actual.getPlanId()).isEqualTo(response.getPlanId());
    }

    @Test
    @DisplayName("저장, 공유 모두 false인 계획 삭제")
    public void test_saved_shared_X_plan_delete() {

        PlanStatus status = PlanStatus.SHARED;
        Plan plan = 저장_공유_X_계획();
        DeletePlanResponse response = 계획_삭제_응답();

        when(planRepository.getById(any(Long.class))).thenReturn(plan);
        when(planRepository.save(any(Plan.class))).thenReturn(plan);

        DeletePlanResponse actual = planService.delete(status, plan.getId());
        verify(planKeywordRepository).deleteByPlanId(plan.getId());
        verify(planCafeRepository).deleteByPlanId(plan.getId());
        verify(planRepository).delete(plan);

        assertThat(actual.getPlanId()).isEqualTo(response.getPlanId());
    }

    @Test
    @DisplayName("계획(여정) 생성 - 목적 키워드를 입력하지 않은 경우")
    public void test_create_plan() {

        CreatePlanRequest request = 계획_요청();
        when(keywordRepository.findByNameIn(키워드_이름_리스트)).thenReturn(Arrays.asList(키워드_목적_X()));

        Assertions.assertThatThrownBy(() -> planService.plan(request))
                .isInstanceOf(NoSuchPlanKeywordException.class);
    }
}