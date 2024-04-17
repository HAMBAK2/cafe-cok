package com.sideproject.hororok.favorite.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.favorite.domain.FavoriteFolder;
import com.sideproject.hororok.favorite.domain.FavoriteFolderRepository;
import com.sideproject.hororok.favorite.domain.FavoriteRepository;
import com.sideproject.hororok.favorite.dto.FavoriteFolderDto;
import com.sideproject.hororok.favorite.dto.response.MyPlaceResponse;
import com.sideproject.hororok.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static com.sideproject.hororok.common.fixtures.FavoriteFolderFixtures.*;
import static com.sideproject.hororok.common.fixtures.LoginMemberFixtures.*;
import static com.sideproject.hororok.common.fixtures.MemberFixtures.사용자;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;


class FavoriteFolderServiceTest{

    @Mock
    private FavoriteFolderRepository favoriteFolderRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteFolderService favoriteFolderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("사용자가 소유한 폴더 정보를 조회")
    public void testMyPlace_withFolders() {

        //given
        LoginMember loginMember = 로그인_맴버();
        when(favoriteFolderRepository
                .countByMemberId(loginMember.getId()))
                .thenReturn(폴더_개수);

        Member member = 사용자();
        FavoriteFolder folder1 = 폴더1(member);
        FavoriteFolder folder2 = 폴더2(member);
        when(favoriteFolderRepository
                .findByMemberId(로그인_맴버_아이디))
                .thenReturn(Arrays.asList(folder1, folder2));

        //when
        MyPlaceResponse response = favoriteFolderService.myPlace(loginMember);

        //then
        assertThat(response.getFolderCount()).isEqualTo(폴더_개수);
        assertThat(response.getFolders().size()).isEqualTo(폴더_리스트_사이즈);
        assertThat(response.getFolders().get(폴더_리스트_인덱스1).getName()).isEqualTo(즐겨찾기_폴더_이름1);
        assertThat(response.getFolders().get(폴더_리스트_인덱스2).getName()).isEqualTo(즐겨찾기_폴더_이름2);
    }

    @Test
    @DisplayName("즐겨찾기 폴더 도메인을 DTO 리스트로 변경하는 기능")
    public void testMapToFavoriteFolderDtoList() {
        // Given
        Member member = 사용자();
        FavoriteFolder folder1 = 폴더1(member);
        FavoriteFolder folder2 = 폴더2(member);
        List<FavoriteFolder> favoriteFolders = Arrays.asList(folder1, folder2);

        // When
        List<FavoriteFolderDto> favoriteFolderDtos = favoriteFolderService.mapToFavoriteFolderDtoList(favoriteFolders);

        // Then
        assertThat(favoriteFolderDtos.size()).isEqualTo(favoriteFolders.size());
        for(int i = 0; i < favoriteFolders.size(); i++) {
            assertThat(favoriteFolderDtos.get(i).getName()).isEqualTo(favoriteFolders.get(i).getName());
            assertThat(favoriteFolderDtos.get(i).getColor()).isEqualTo(favoriteFolders.get(i).getColor());
            assertThat(favoriteFolderDtos.get(i).getFolderId()).isEqualTo(favoriteFolders.get(i).getId());
        }
    }
}