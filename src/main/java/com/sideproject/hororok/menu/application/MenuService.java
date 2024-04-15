package com.sideproject.hororok.menu.application;


import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.menu.dto.MenuDto;
import com.sideproject.hororok.menu.domain.Menu;
import com.sideproject.hororok.menu.domain.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    @LogTrace
    public List<MenuDto> findByCafeId(Long cafeId) {

        List<Menu> menuList = menuRepository.findByCafeId(cafeId);
        List<MenuDto> menuDtos = new ArrayList<>();
        for (Menu menu : menuList) {
            menuDtos.add(MenuDto.from(menu));
        }

        return menuDtos;
    }

}
