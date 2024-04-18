package com.sideproject.hororok.menu.domain;

import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @LogTrace
    List<Menu> findByCafeId(Long cafeId);
}
