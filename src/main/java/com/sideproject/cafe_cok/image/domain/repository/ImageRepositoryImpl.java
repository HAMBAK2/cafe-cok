package com.sideproject.cafe_cok.image.domain.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.image.dto.ImageUrlCursorDto;
import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import com.sideproject.cafe_cok.image.dto.QImageUrlCursorDto;
import com.sideproject.cafe_cok.image.dto.QImageUrlDto;
import com.sideproject.cafe_cok.utils.QuerydslUtil;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.sideproject.cafe_cok.image.domain.QImage.*;
import static org.springframework.util.StringUtils.isEmpty;

public class ImageRepositoryImpl implements ImageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ImageRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ImageUrlDto> findCafeImageUrlDtoListByCafeId(final Long cafeId,
                                                             final Pageable pageable) {

        return queryFactory
                .select(new QImageUrlDto(
                        image.origin,
                        image.thumbnail
                ))
                .from(image)
                .where(cafeIdEq(cafeId),
                        image.review.id.isNull(),
                        image.menu.id.isNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(image.imageType.asc())
                .fetch();
    }

    @Override
    public List<ImageUrlDto> findImageUrlDtoListByReviewId(final Long reviewId) {

        return queryFactory
                .select(new QImageUrlDto(
                        image.origin,
                        image.thumbnail
                ))
                .from(image)
                .where(image.review.id.eq(reviewId))
                .fetch();
    }

    @Override
    public List<ImageUrlDto> findImageUrlDtoListByCafeIdImageType(final Long cafeId,
                                                                  final ImageType imageType) {

        return queryFactory
                .select(new QImageUrlDto(
                        image.origin,
                        image.thumbnail
                ))
                .from(image)
                .where(cafeIdEq(cafeId),
                        image.imageType.eq(imageType))
                .fetch();
    }

    @Override
    public List<ImageUrlDto> findImageUrlDtoListByCafeIdImageType(final Long cafeId,
                                                                  final ImageType imageType,
                                                                  final Pageable pageable) {
        return queryFactory
                .select(new QImageUrlDto(
                        image.origin,
                        image.thumbnail
                ))
                .from(image)
                .where(cafeIdEq(cafeId),
                        image.imageType.eq(imageType))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<ImageUrlDto> findImageUrlDtoListByReviewId(final Long reviewId,
                                                           final Pageable pageable) {

        NumberPath<Long> idPath = image.id;
        List<OrderSpecifier<?>> orderSpecifiers = QuerydslUtil.getOrderSpecifiers(pageable, idPath);

        return queryFactory
                .select(new QImageUrlDto(
                        image.origin,
                        image.thumbnail
                ))
                .from(image)
                .where(image.review.id.eq(reviewId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .fetch();
    }

    @Override
    public List<ImageUrlCursorDto> findImageUrlCursorDtoListByCafeIdAndImageType(final Long cafeId,
                                                                                 final Long cursor,
                                                                                 final ImageType imageType,
                                                                                 final Pageable pageable) {
        NumberPath<Long> idPath = image.id;
        List<OrderSpecifier<?>> orderSpecifiers = QuerydslUtil.getOrderSpecifiers(pageable, idPath);

        return queryFactory
                .select(new QImageUrlCursorDto(
                        image.origin,
                        image.thumbnail,
                        image.id
                ))
                .from(image)
                .where(cafeIdEq(cafeId),
                        image.imageType.eq(imageType),
                        imageIdLt(cursor))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .fetch();
    }

    private BooleanExpression cafeIdEq(final Long cafeId) {
        return isEmpty(cafeId) ? null : image.cafe.id.eq((cafeId));
    }

    private BooleanExpression imageIdLt(final Long cursor) {
        return isEmpty(cursor) ? null : image.id.lt(cursor);
    }
}
