package com.sideproject.hororok.menu.repository;

import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @LogTrace
    List<Menu> findByCafeId(Long cafeId);
}
