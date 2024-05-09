package com.sideproject.cafe_cok.common.fixtures;

import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.dto.MenuDto;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static com.sideproject.cafe_cok.common.fixtures.CafeFixtures.카페;


public class MenuFixtures {

    public static final Long 메뉴_ID = 1L;
    public static final String 메뉴_이름 = "메뉴이름";
    public static final Integer 메뉴_가격 = 10000;
    public static final String 메뉴_이미지_URL = "//메뉴 이미지 경로";


    public static Menu 메뉴() {
        Menu menu = new Menu(메뉴_이름, 메뉴_가격, 메뉴_이미지_URL, 카페());
        setMenuId(menu, 메뉴_ID);
        return menu;
    }

    public static MenuDto 메뉴_DTO() {
        return MenuDto.from(메뉴());
    }

    public static List<MenuDto> 메뉴_DTO_리스트() {
        return Arrays.asList(메뉴_DTO());
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
