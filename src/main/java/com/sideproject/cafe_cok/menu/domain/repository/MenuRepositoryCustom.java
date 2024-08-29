package com.sideproject.cafe_cok.menu.domain.repository;

import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.dto.MenuImageUrlDto;

import java.util.List;

public interface MenuRepositoryCustom {

    /** TODO: 이 메서드 테스트 코드 작성해야 함 **/
    List<MenuImageUrlDto> findMenuImageUrlDtoListByCafeId(final Long cafeId);

}
