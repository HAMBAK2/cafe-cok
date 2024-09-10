package com.sideproject.cafe_cok.menu.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.menu.dto.MenuImageDto;
import com.sideproject.cafe_cok.menu.dto.QMenuImageDto;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.sideproject.cafe_cok.image.domain.QImage.image;
import static com.sideproject.cafe_cok.menu.domain.QMenu.*;

public class MenuRepositoryImpl implements MenuRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MenuRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MenuImageDto> getMenuImageUrls(final Long cafeId) {
        return queryFactory
                .select(new QMenuImageDto(menu.name, menu.price, image.origin, image.thumbnail))
                .from(menu)
                .leftJoin(image).on(image.menu.id.eq(menu.id))
                .where(menu.cafe.id.eq(cafeId))
                .fetch();
    }
}
