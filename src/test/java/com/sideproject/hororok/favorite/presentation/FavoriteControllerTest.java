package com.sideproject.hororok.favorite.presentation;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.common.annotation.ControllerTest;
import com.sideproject.hororok.favorite.dto.FavoriteFolderDto;
import com.sideproject.hororok.favorite.dto.response.MyPlaceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static com.sideproject.hororok.common.fixtures.FavoriteFolderFixtures.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


class FavoriteControllerTest extends ControllerTest {



    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_HEADER_VALUE = "Bearer fake-token";

    @Test
    @DisplayName("하단 탭의 \"저장\" 버튼을 눌렀을 때 필요한 정보 제공한다.")
    public void testMyPlace() throws Exception {

        List<FavoriteFolderDto> fakeFolders = new ArrayList<>();
        FavoriteFolderDto favoriteFolderDto1 =
                폴더_Dto(폴더_있을때_ID_1, 즐겨찾기_폴더_이름1,
                        즐겨찾기_폴더_색상1, 즐겨찾기_폴더_노출여부1);

        FavoriteFolderDto favoriteFolderDto2 =
                폴더_Dto(폴더_있을때_ID_2, 즐겨찾기_폴더_이름2,
                        즐겨찾기_폴더_색상2, 즐겨찾기_폴더_노출여부2);
        fakeFolders.add(favoriteFolderDto1);
        fakeFolders.add(favoriteFolderDto2);

        MyPlaceResponse fakeResponse = 마이_플레이스_응답(폴더_있을때_개수, fakeFolders);


        when(favoriteFolderService
                .myPlace(any(LoginMember.class)))
                .thenReturn(fakeResponse);

        mockMvc.perform(
                    get("/api/favorite/myPlace")
                            .header(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(폴더_있을때_개수))
                .andExpect(jsonPath("$.folders").isArray())
                .andExpect(jsonPath("$.folders", hasSize(폴더_있을때_리스트_사이즈)))
                .andExpect(jsonPath("$.folders["+폴더_있을때_리스트_인덱스1+"].folderId").value(폴더_있을때_ID_1))
                .andExpect(jsonPath("$.folders["+폴더_있을때_리스트_인덱스1+"].name").value(즐겨찾기_폴더_이름1))
                .andExpect(jsonPath("$.folders["+폴더_있을때_리스트_인덱스2+"].folderId").value(폴더_있을때_ID_2))
                .andExpect(jsonPath("$.folders["+폴더_있을때_리스트_인덱스2+"].name").value(즐겨찾기_폴더_이름2));
    }

}