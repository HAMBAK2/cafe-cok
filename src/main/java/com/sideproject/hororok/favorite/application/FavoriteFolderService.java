package com.sideproject.hororok.favorite.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.favorite.domain.FavoriteFolder;
import com.sideproject.hororok.favorite.domain.FavoriteFolderRepository;
import com.sideproject.hororok.favorite.domain.FavoriteRepository;
import com.sideproject.hororok.favorite.dto.FavoriteFolderDto;
import com.sideproject.hororok.favorite.dto.response.MyPlaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteFolderService {

    private final FavoriteFolderRepository favoriteFolderRepository;
    private final FavoriteRepository favoriteRepository;

    public MyPlaceResponse myPlace(LoginMember loginMember) {

        Long memberId = loginMember.getId();
        Long count = favoriteFolderRepository.countByMemberId(memberId);

        List<FavoriteFolderDto> folders =
                mapToFavoriteFolderDtoList(favoriteFolderRepository.findByMemberId(memberId));

        return new MyPlaceResponse(count, folders);
    }

    public List<FavoriteFolderDto> mapToFavoriteFolderDtoList(List<FavoriteFolder> folders) {

        return folders.stream()
                .map(folder ->
                        FavoriteFolderDto
                                .of(folder, favoriteRepository.countByFavoriteFolderId(folder.getId())))
                .collect(Collectors.toList());
    }




}
