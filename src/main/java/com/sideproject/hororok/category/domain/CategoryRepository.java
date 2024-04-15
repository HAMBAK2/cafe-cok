package com.sideproject.hororok.category.domain;


import com.sideproject.hororok.aop.annotation.LogTrace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @LogTrace
    List<Category> findAll();
}
