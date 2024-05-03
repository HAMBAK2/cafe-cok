package com.sideproject.hororok.menu.domain.repository;

import com.sideproject.hororok.menu.domain.Menu;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByCafeId(final Long cafeId);

    List<Menu> findByCafeId(final Long cafeId, final Pageable pageable);
}
