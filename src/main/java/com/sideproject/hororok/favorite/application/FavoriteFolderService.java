package com.sideproject.hororok.favorite.application;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.favorite.domain.FavoriteFolder;
import com.sideproject.hororok.favorite.domain.FavoriteFolderRepository;
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

    public MyPlaceResponse myPlace(LoginMember loginMember) {

        Long memberId = loginMember.getId();
        Long count = countByMemberId(memberId);

        List<FavoriteFolderDto> folders =
                mapToFavoriteFolderDtoList(findByMemberId(memberId));

        return new MyPlaceResponse(count, folders);
    }

    public Long countByMemberId(Long memberId) {
        return favoriteFolderRepository.countByMemberId(memberId);
    }

    public List<FavoriteFolder> findByMemberId(Long memberId){
        return favoriteFolderRepository.findByMemberId(memberId);
    }

    public List<FavoriteFolderDto> mapToFavoriteFolderDtoList(List<FavoriteFolder> folders) {

        return folders.stream()
                .map(folder -> FavoriteFolderDto.from(folder))
                .collect(Collectors.toList());
    }




}
