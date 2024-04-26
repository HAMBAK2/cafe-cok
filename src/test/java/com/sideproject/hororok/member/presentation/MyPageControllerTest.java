package com.sideproject.hororok.member.presentation;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.common.annotation.ControllerTest;
import com.sideproject.hororok.member.dto.response.MyPagePlanDetailResponse;
import com.sideproject.hororok.member.dto.response.MyPagePlanResponse;
import com.sideproject.hororok.member.dto.response.MyPageProfileResponse;
import com.sideproject.hororok.member.dto.response.MyPageTagSaveResponse;
import com.sideproject.hororok.plan.domain.enums.PlanSortBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.sideproject.hororok.common.fixtures.MemberFixtures.마이페이지_계획_상세_응답;
import static com.sideproject.hororok.common.fixtures.MyPageFixtures.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MyPageControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer fake-token";

    @Test
    @DisplayName("마이페이지 상단의 프로필을 호출하는 API - 성공")
    public void test_myPage_profile_success() throws Exception{

        MyPageProfileResponse response = 마이페이지_프로필_응답();

        when(myPageService
                .profile(any(LoginMember.class)))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/myPage/profile")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("myPage/profile/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        responseFields(
                                fieldWithPath("nickname").description("사용자 닉네임"),
                                fieldWithPath("picture").description("사용자 프로필 사진 URL"),
                                fieldWithPath("reviewCount").description("리뷰 개수"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("마이페이지 저장 탭을 눌렀을 때 동작 하는 API - 성공")
    public void test_myPage_tag_save_success() throws Exception {

        MyPageTagSaveResponse response = 마이페이지_태그_저장_응답();

        when(myPageService
                .tagSave(any(LoginMember.class)))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/myPage/tag/save")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("myPage/tag/save/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        responseFields(
                                fieldWithPath("folderCount").description("폴더 개수"),
                                fieldWithPath("folders").description("폴더 리스트").type(JsonFieldType.ARRAY),
                                fieldWithPath("folders[].folderId").description("폴더 ID"),
                                fieldWithPath("folders[].name").description("폴더 이름"),
                                fieldWithPath("folders[].color").description("폴더 색상"),
                                fieldWithPath("folders[].bookmarkCount").description("폴더 내 북마크 개수"),
                                fieldWithPath("folders[].visible").description("폴더 지도 노출 여부"),
                                fieldWithPath("folders[].defaultFolder").description("기본 폴더 여부"))))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("마이페이지 계획 탭의 저장한 계획(여정) 나타내는 API - 성공")
    public void test_saved_plan_success() throws Exception {

        MyPagePlanResponse response = 마이페이지_계획_응답();

        when(myPageService.savedPlan(any(LoginMember.class), any(PlanSortBy.class)))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/myPage/saved/plan?sortBy="+PlanSortBy.RECENT.name())
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("myPage/saved/plan/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        queryParameters(
                                parameterWithName("sortBy")
                                        .description("정렬 방식을 구분하는 필드\n\n" +
                                                "- RECENT: 최신 저장 순(Default)\n\n" + "- UPCOMING: 다가오는 여정 순")),
                        responseFields(
                                fieldWithPath("plans").type(JsonFieldType.ARRAY).description("계획(어정) 리스트"),
                                fieldWithPath("plans[].id").description("계획(여정)의 ID"),
                                fieldWithPath("plans[].keyword").type(JsonFieldType.OBJECT).description("계획(여정)의 대표 키워드"),
                                fieldWithPath("plans[].keyword.id").description("키워드 ID"),
                                fieldWithPath("plans[].keyword.category").description("키워드의 카테고리"),
                                fieldWithPath("plans[].keyword.name").description("키워드의 이름"),
                                fieldWithPath("plans[].location").description("사용자가 검색한 장소_필수X"),
                                fieldWithPath("plans[].visitDateTime").description("사용자가 검색한 날짜와 시간_필수X"))))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("마이페이지 계획 탭의 공유한 계획(여정)을 나타내는 API - 성공")
    public void test_shared_plan_success() throws Exception {

        MyPagePlanResponse response = 마이페이지_계획_응답();

        when(myPageService.sharedPlan(any(LoginMember.class), any(PlanSortBy.class)))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/myPage/shared/plan?sortBy="+PlanSortBy.RECENT.name())
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("myPage/shared/plan/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        queryParameters(
                                parameterWithName("sortBy")
                                        .description("정렬 방식을 구분하는 필드\n\n" +
                                                "- RECENT: 최신 저장 순(Default)\n\n" + "- UPCOMING: 다가오는 여정 순")),
                        responseFields(
                                fieldWithPath("plans").type(JsonFieldType.ARRAY).description("계획(어정) 리스트"),
                                fieldWithPath("plans[].id").description("계획(여정)의 ID"),
                                fieldWithPath("plans[].keyword").type(JsonFieldType.OBJECT).description("계획(여정)의 대표 키워드"),
                                fieldWithPath("plans[].keyword.id").description("키워드 ID"),
                                fieldWithPath("plans[].keyword.category").description("키워드의 카테고리"),
                                fieldWithPath("plans[].keyword.name").description("키워드의 이름"),
                                fieldWithPath("plans[].location").description("사용자가 검색한 장소_필수X"),
                                fieldWithPath("plans[].visitDateTime").description("사용자가 검색한 날짜와 시간_필수X"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("저장된 계획의 전체 리스트를 나타내는 API - 성공")
    public void test_saved_plans_success() throws Exception {

        MyPagePlanResponse response = 마이페이지_계획_응답();

        when(myPageService
                .savedPlans(any(LoginMember.class), any(PlanSortBy.class), any(Integer.class), any(Integer.class)))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/myPage/saved/plans?sortBy="+PlanSortBy.RECENT.name()+"&page=1&size=10")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("myPage/saved/plans/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        queryParameters(
                                parameterWithName("sortBy")
                                        .description("정렬 방식을 구분하는 필드\n\n" +
                                                "- RECENT: 최신 저장 순(Default)\n\n" + "- UPCOMING: 다가오는 여정 순"),
                                parameterWithName("page").description("페이지 번호를 지정(Default = 1)"),
                                parameterWithName("size").description("페이지의 사이즈를 지정(Default = 10)")),
                        responseFields(
                                fieldWithPath("plans").type(JsonFieldType.ARRAY).description("계획(어정) 리스트"),
                                fieldWithPath("plans[].id").description("계획(여정)의 ID"),
                                fieldWithPath("plans[].keyword").type(JsonFieldType.OBJECT).description("계획(여정)의 대표 키워드"),
                                fieldWithPath("plans[].keyword.id").description("키워드 ID"),
                                fieldWithPath("plans[].keyword.category").description("키워드의 카테고리"),
                                fieldWithPath("plans[].keyword.name").description("키워드의 이름"),
                                fieldWithPath("plans[].location").description("사용자가 검색한 장소_필수X"),
                                fieldWithPath("plans[].visitDateTime").description("사용자가 검색한 날짜와 시간_필수X"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("공유된 계획의 전체 리스트를 나타내는 API - 성공")
    public void test_shared_plans_success() throws Exception {

        MyPagePlanResponse response = 마이페이지_계획_응답();

        when(myPageService
                .sharedPlans(any(LoginMember.class), any(PlanSortBy.class), any(Integer.class), any(Integer.class)))
                .thenReturn(response);

        mockMvc.perform(
                        get("/api/myPage/shared/plans?sortBy="+PlanSortBy.RECENT.name()+"&page=1&size=10")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("myPage/shared/plans/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        queryParameters(
                                parameterWithName("sortBy")
                                        .description("정렬 방식을 구분하는 필드\n\n" +
                                                "- RECENT: 최신 저장 순(Default)\n\n" + "- UPCOMING: 다가오는 여정 순"),
                                parameterWithName("page").description("페이지 번호를 지정(Default = 1)"),
                                parameterWithName("size").description("페이지의 사이즈를 지정(Default = 10)")),
                        responseFields(
                                fieldWithPath("plans").type(JsonFieldType.ARRAY).description("계획(어정) 리스트"),
                                fieldWithPath("plans[].id").description("계획(여정)의 ID"),
                                fieldWithPath("plans[].keyword").type(JsonFieldType.OBJECT).description("계획(여정)의 대표 키워드"),
                                fieldWithPath("plans[].keyword.id").description("키워드 ID"),
                                fieldWithPath("plans[].keyword.category").description("키워드의 카테고리"),
                                fieldWithPath("plans[].keyword.name").description("키워드의 이름"),
                                fieldWithPath("plans[].location").description("사용자가 검색한 장소_필수X"),
                                fieldWithPath("plans[].visitDateTime").description("사용자가 검색한 날짜와 시간_필수X"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("마이페이지 계획텝에서 하나의 계획(여정)을 선택했을 때 동작 - 성공")
    public void test_my_page_detail_success() throws Exception{


        MyPagePlanDetailResponse response = 마이페이지_계획_상세_응답();

        when(myPageService.planDetail(any(Long.class)))
                .thenReturn(response);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/api/myPage/plan/{planId}", response.getPlanId())
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(document("myPage/plan/detail/success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer JWT 엑세스 토큰")),
                        pathParameters(
                                parameterWithName("planId").description("선택한 계획(여정)의 ID")),
                        responseFields(
                                fieldWithPath("planId").description("계획 ID"),
                                fieldWithPath("matchType").description("계획하기 결과 타입(MATCH, SIMILAR, MISMATCH)"),
                                fieldWithPath("locationName").description("검색한 위치 이름"),
                                fieldWithPath("minutes").description("도보 시간"),
                                fieldWithPath("visitDateTime").description("방문 날짜와 시간(월-일-시간-분"),
                                fieldWithPath("categoryKeywords").type(JsonFieldType.OBJECT).description("카테고리 별 키워드 정보"),
                                fieldWithPath("categoryKeywords.purpose").type(JsonFieldType.ARRAY).description("목적 카테고리 리스트(필수)"),
                                fieldWithPath("categoryKeywords.menu").type(JsonFieldType.ARRAY).description("메뉴 카테고리 리스트(없으면 빈 리스트)"),
                                fieldWithPath("categoryKeywords.theme").type(JsonFieldType.ARRAY).description("테마 카테고리 리스트(없으면 빈 리스트)"),
                                fieldWithPath("categoryKeywords.facility").type(JsonFieldType.ARRAY).description("시설 카테고리 리스트(없으면 빈 리스트)"),
                                fieldWithPath("categoryKeywords.atmosphere").type(JsonFieldType.ARRAY).description("분위기 카테고리 리스트(없으면 빈 리스트)"),
                                fieldWithPath("similarCafes").type(JsonFieldType.ARRAY)
                                        .description("유사한 카페(결과 타입이 MATCH, SIMILAR인 경우 존재, 아닌 경우 빈 리스트)"),
                                fieldWithPath("similarCafes[].id").description("카페 ID"),
                                fieldWithPath("similarCafes[].name").description("카페 이름"),
                                fieldWithPath("similarCafes[].phoneNumber").description("전화번호"),
                                fieldWithPath("similarCafes[].roadAddress").description("도로명 주소"),
                                fieldWithPath("similarCafes[].longitude").description("경도"),
                                fieldWithPath("similarCafes[].latitude").description("위도"),
                                fieldWithPath("similarCafes[].starRating").description("별점"),
                                fieldWithPath("similarCafes[].reviewCount").description("리뷰 수"),
                                fieldWithPath("similarCafes[].image").description("카페 이미지 URL"),
                                fieldWithPath("matchCafes").type(JsonFieldType.ARRAY)
                                        .description("일치하는 카페(결과 타입이 MATCH인 경우 존재, 아닌 경우 빈 리스트, 형식은 유사한 카페와 동일)"))))
                .andExpect(status().isOk());
    }
}