package com.sideproject.cafe_cok.menu.domain.repository;

import com.sideproject.cafe_cok.menu.domain.Menu;
import com.sideproject.cafe_cok.menu.exception.NoSuchMenuException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuRepositoryCustom {

    List<Menu> findByCafeId(final Long cafeId);

    default Menu getById(final Long id) {
        return findById(id)
                .orElseThrow(() ->
                        new NoSuchMenuException("[ID : " + id + "] 에 해당하는 메뉴가 존재하지 않습니다."));
    }
}
