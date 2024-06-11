package com.sideproject.cafe_cok.menu.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.image.domain.QImage;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.menu.domain.QMenu;
import com.sideproject.cafe_cok.menu.dto.MenuImageUrlDto;
import com.sideproject.cafe_cok.menu.dto.QMenuImageUrlDto;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.sideproject.cafe_cok.image.domain.QImage.image;
import static com.sideproject.cafe_cok.menu.domain.QMenu.*;
import static com.sideproject.cafe_cok.plan.domain.QPlanCafe.planCafe;
import static org.springframework.util.StringUtils.isEmpty;

public class MenuRepositoryImpl implements MenuRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public MenuRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MenuImageUrlDto> findMenuImageUrlDtoListByCafeId(final Long cafeId) {
        return queryFactory
                .select(new QMenuImageUrlDto(menu, image))
                .from(menu)
                .leftJoin(image).on(image.menu.id.eq(menu.id))
                .where(menu.cafe.id.eq(cafeId))
                .fetch();
    }
}
