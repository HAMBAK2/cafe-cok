package com.sideproject.hororok.menu.application;


import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.menu.dto.MenuInfo;
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
    public List<MenuInfo> findByCafeId(Long cafeId) {

        List<Menu> menuList = menuRepository.findByCafeId(cafeId);
        List<MenuInfo> menuInfos = new ArrayList<>();
        for (Menu menu : menuList) {
            menuInfos.add(MenuInfo.from(menu));
        }

        return menuInfos;
    }

}
