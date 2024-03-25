package com.sideproject.hororok.Menu.repository;

import com.sideproject.hororok.Menu.dto.MenuDto;
import com.sideproject.hororok.Menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByCafeId(Long cafeId);
}
