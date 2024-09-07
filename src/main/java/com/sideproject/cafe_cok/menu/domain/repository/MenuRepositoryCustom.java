package com.sideproject.cafe_cok.menu.domain.repository;

import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.dto.MenuImageUrlDto;

import java.util.List;

public interface MenuRepositoryCustom {

    List<MenuImageUrlDto> getMenuImageUrls(final Long cafeId);

}
