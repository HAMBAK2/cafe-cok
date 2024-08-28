package com.sideproject.cafe_cok.menu.application;

import com.sideproject.cafe_cok.menu.dto.response.MenuListResponse;
import com.sideproject.cafe_cok.menu.domain.repository.MenuRepository;
import com.sideproject.cafe_cok.menu.dto.MenuImageUrlDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuListResponse findByCafeId(final Long cafeId) {

        List<MenuImageUrlDto> findMenus = menuRepository.findMenuImageUrlDtoListByCafeId(cafeId);
        return new MenuListResponse(findMenus);
    }
}
