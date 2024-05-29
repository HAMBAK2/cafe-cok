package com.sideproject.cafe_cok.cafe.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.cafe.dto.CafeDto;
import com.sideproject.cafe_cok.image.domain.enums.ImageType;
import com.sideproject.cafe_cok.plan.domain.enums.PlanCafeMatchType;
import com.sideproject.cafe_cok.plan.dto.request.CreatePlanRequest;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.sideproject.cafe_cok.cafe.domain.QCafe.*;
import static com.sideproject.cafe_cok.cafe.domain.QOperationHour.*;
import static com.sideproject.cafe_cok.image.domain.QImage.*;
import static com.sideproject.cafe_cok.keword.domain.QCafeReviewKeyword.*;
import static com.sideproject.cafe_cok.keword.domain.QKeyword.*;
import static com.sideproject.cafe_cok.plan.domain.QPlanCafe.*;
import static com.sideproject.cafe_cok.utils.Constants.*;
import static com.sideproject.cafe_cok.utils.GeometricUtils.*;
import static org.springframework.util.StringUtils.isEmpty;

public class CafeRepositoryImpl implements CafeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CafeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Cafe> findNotMismatchCafes(final CreatePlanRequest request) {

        return queryFactory
                .select(cafe)
                .from(operationHour)
                .leftJoin(operationHour.cafe, cafe)
                .leftJoin(cafe.cafeReviewKeywords, cafeReviewKeyword)
                .leftJoin(cafeReviewKeyword.keyword, keyword)
                .where(
                        dateEq(request.getDate()),
                        openingTimeLoe(request.getStartTime()),
                        closingTimeGoe(request.getEndTime()),
                        keywordNamesIn(request.getKeywords()),
                        isWithinRadius(request.getLatitude(), request.getLongitude(), request.getMinutes()))
                .fetch();
    }

    @Override
    public List<Cafe> findWithinRadiusCafeList(final BigDecimal latitude,
                                               final BigDecimal longitude) {
        return queryFactory
                .select(cafe)
                .from(cafe)
                .leftJoin(image).on(cafe.id.eq(image.cafe.id))
                .where(isWithinRadius(latitude, longitude, MAX_RADIUS_TIME))
                .fetch();

    }

    @Override
    public List<Cafe> findByPlanIdAndMatchType(final Long planId,
                                               final PlanCafeMatchType matchType) {

        return queryFactory
                .select(cafe)
                .from(planCafe)
                .leftJoin(planCafe.cafe, cafe)
                .leftJoin(image).on(cafe.id.eq(image.cafe.id))
                .where(planIdEq(planId),
                        matchTypeEq(matchType))
                .fetch();
    }


    private BooleanExpression imageTypeEq(final ImageType imageType) {
        return isEmpty(imageType) ? null : image.imageType.eq(imageType);
    }

    private BooleanExpression matchTypeEq(final PlanCafeMatchType matchType) {
        return isEmpty(matchType) ? null : planCafe.matchType.eq(matchType);
    }

    private BooleanExpression planIdEq(final Long id) {
        return isEmpty(id) ? null : planCafe.plan.id.eq(id);
    }

    private BooleanExpression dateEq(final LocalDate date) {
        return isEmpty(date) ? null : operationHour.date.eq(date.getDayOfWeek());
    }

    private BooleanExpression openingTimeLoe(final LocalTime timeGoe) {
        return isEmpty(timeGoe) ? null : operationHour.openingTime.loe(timeGoe);
    }

    private BooleanExpression closingTimeGoe(final LocalTime timeGoe) {
        return isEmpty(timeGoe) ? null : operationHour.closingTime.goe(timeGoe);
    }

    private BooleanExpression keywordNamesIn(final List<String> keywordNames) {
        return isEmpty(keywordNames) ? null : keyword.name.in(keywordNames);
    }

    private BooleanExpression isWithinRadius(final BigDecimal latitude,
                                             final BigDecimal longitude,
                                             final Integer minutes) {

        if(isEmpty(latitude) || isEmpty(longitude)) return null;

        double radiusInKm;
        if(isEmpty(minutes)) radiusInKm = calculateRadiusInKm(MAX_RADIUS_TIME);
        else radiusInKm = calculateRadiusInKm(minutes);

        NumberExpression<BigDecimal> radiansLatitude =
                Expressions.numberTemplate(BigDecimal.class, "radians({0})", latitude);

        //cos 계산
        NumberExpression<BigDecimal> cosLatitude =
                Expressions.numberTemplate(BigDecimal.class, "cos({0})", radiansLatitude);
        NumberExpression<BigDecimal> cosCafeLatitude =
                Expressions.numberTemplate(BigDecimal.class, "cos(radians({0}))", cafe.latitude);

        //sin 계산
        NumberExpression<BigDecimal> sinLatitude =
                Expressions.numberTemplate(BigDecimal.class, "sin({0})", radiansLatitude);
        NumberExpression<BigDecimal> sinCafeLatitude =
                Expressions.numberTemplate(BigDecimal.class, "sin(radians({0}))", cafe.latitude);

        // 사이 거리 계산
        NumberExpression<BigDecimal> cosLongitude =
                Expressions.numberTemplate(BigDecimal.class, "cos(radians({0}) - radians({1}))", cafe.longitude, longitude);
        NumberExpression<BigDecimal> acosExpression =
                Expressions.numberTemplate(BigDecimal.class, "acos({0})",
                        cosLatitude.multiply(cosCafeLatitude).multiply(cosLongitude).add(sinLatitude.multiply(sinCafeLatitude)));

        //최종 거리 계산
        NumberExpression<Double> distanceExpression =
                Expressions.numberTemplate(Double.class, "6371 * {0}", acosExpression);

        return distanceExpression.loe(radiusInKm);
    }
}
