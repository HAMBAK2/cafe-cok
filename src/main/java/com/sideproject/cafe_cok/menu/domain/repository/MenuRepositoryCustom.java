package com.sideproject.cafe_cok.menu.domain.repository;

import com.sideproject.cafe_cok.menu.dto.MenuImageDto;

import java.util.List;

public interface MenuRepositoryCustom {

    List<MenuImageDto> getMenuImageUrls(final Long cafeId);

}
