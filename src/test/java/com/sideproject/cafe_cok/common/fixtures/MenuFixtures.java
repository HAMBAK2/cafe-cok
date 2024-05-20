package com.sideproject.cafe_cok.common.fixtures;

import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.dto.MenuDto;
import com.sideproject.cafe_cok.menu.dto.MenuImageUrlDto;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static com.sideproject.cafe_cok.common.fixtures.CafeFixtures.카페;
import static com.sideproject.cafe_cok.common.fixtures.ImageFixtures.메뉴_이미지;


public class MenuFixtures {

    public static final Long 메뉴_ID = 1L;
    public static final String 메뉴_이름 = "메뉴이름";
    public static final Integer 메뉴_가격 = 10000;
    public static final String 메뉴_원본_이미지_URL = "//메뉴 원본 이미지 경로";
    public static final String 메뉴_썸네일_이미지_URL = "//메뉴 썸네일 이미지 경로";


    public static Menu 메뉴() {
        Menu menu = new Menu(메뉴_이름, 메뉴_가격, 카페());
        setMenuId(menu, 메뉴_ID);
        return menu;
    }

    public static MenuImageUrlDto 메뉴_이미지_DTO() {
        return new MenuImageUrlDto(메뉴(), 메뉴_이미지(ImageType.MENU, 카페(), 메뉴()));
    }

    public static List<MenuImageUrlDto> 메뉴_이미지_DTO_리스트() {
        return Arrays.asList(메뉴_이미지_DTO());
    }

    public static Menu setMenuId(Menu menu, final Long id) {

        try {
            Field idField = Menu.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(menu, id);
            return menu;
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
