package com.sideproject.hororok.Menu.service;


import com.sideproject.hororok.Menu.dto.MenuDto;
import com.sideproject.hororok.Menu.entity.Menu;
import com.sideproject.hororok.Menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public List<MenuDto> findByCafeId(Long cafeId) {

        List<Menu> menuList = menuRepository.findByCafeId(cafeId);
        List<MenuDto> menuDtos = new ArrayList<>();
        for (Menu menu : menuList) {
            menuDtos.add(MenuDto.from(menu));
        }

        return menuDtos;
    }

}
