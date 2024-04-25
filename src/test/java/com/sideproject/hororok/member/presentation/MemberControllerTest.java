package com.sideproject.hororok.member.presentation;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.common.annotation.ControllerTest;
import com.sideproject.hororok.member.dto.response.MyPageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;


import static com.sideproject.hororok.common.fixtures.MemberFixtures.마이페이지_조회_응답;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTest {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer fake-token";

    @Captor
    private ArgumentCaptor<LoginMember> loginMemberCaptor;

    @Test
    @DisplayName("마이 페이지 버튼을 클릭하면 MemberMyPageResponse와 200Ok를 반환한다.")
    public void test_my_page() throws Exception {

        MyPageResponse response = 마이페이지_조회_응답();
        when(memberService.myPage(any(LoginMember.class)))
                .thenReturn(response);


        mockMvc.perform(
                        get("/api/member/myPage")
                                .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(response.getNickname()))
                .andExpect(jsonPath("$.folderCount").value(response.getFolderCount()))
                .andExpect(jsonPath("$.reviewCount").value(response.getReviewCount()))
                .andExpect(jsonPath("$.picture").value(response.getPicture()))
                .andExpect(jsonPath("$.folders", hasSize(response.getFolders().size())));
    }

}