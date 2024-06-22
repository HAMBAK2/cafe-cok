package com.sideproject.cafe_cok.menu.domain.repository;

import com.sideproject.cafe_cok.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuRepositoryCustom {

    List<Menu> findByCafeId(final Long cafeId);
}
