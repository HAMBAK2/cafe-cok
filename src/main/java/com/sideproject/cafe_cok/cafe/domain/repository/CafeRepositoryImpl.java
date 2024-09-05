package com.sideproject.cafe_cok.cafe.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.cafe.condition.CafeSearchCondition;
import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.plan.domain.enums.MatchType;
import com.sideproject.cafe_cok.util.QuerydslUtil;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.sideproject.cafe_cok.cafe.domain.QCafe.*;
import static com.sideproject.cafe_cok.cafe.domain.QOperationHour.*;
import static com.sideproject.cafe_cok.plan.domain.QPlanCafe.*;
import static com.sideproject.cafe_cok.util.Constants.*;
import static com.sideproject.cafe_cok.util.GeometricUtil.*;
import static org.springframework.util.StringUtils.isEmpty;

public class CafeRepositoryImpl implements CafeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CafeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Cafe> findByDateAndTime(final CafeSearchCondition searchCondition,
                                        final Sort sort) {

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(dateEq(searchCondition.getDate()));
        conditions.and(openingTimeLoe(searchCondition.getStartTime()));
        if(!searchCondition.getEndTime().equals(LocalTime.MIDNIGHT)) conditions.and(closingTimeGoe(searchCondition.getEndTime()));

        NumberPath<BigDecimal> starRatingPath = cafe.starRating;
        List<OrderSpecifier<?>> orderSpecifiers = QuerydslUtil.getOrderSpecifiers(sort, starRatingPath);

        return queryFactory
                .select(cafe)
                .from(cafe)
                .leftJoin(cafe.operationHours, operationHour)
                .where(conditions)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .fetch();
    }


    @Override
    public List<Cafe> findByDateAndTimeAndMinutes(final CafeSearchCondition searchCondition) {
        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(dateEq(searchCondition.getDate()));
        conditions.and(openingTimeLoe(searchCondition.getStartTime()));
        if(!searchCondition.getEndTime().equals(LocalTime.MIDNIGHT)) conditions.and(closingTimeGoe(searchCondition.getEndTime()));
        conditions.and(isWithinRadius(searchCondition.getLatitude(), searchCondition.getLongitude(), searchCondition.getMinutes()));

        return queryFactory
                .select(cafe)
                .from(cafe)
                .leftJoin(cafe.operationHours, operationHour)
                .where(conditions)
                .fetch();
    }

    @Override
    public List<Cafe> findByPlanIdAndMatchType(final Long planId,
                                               final MatchType matchType) {

        return queryFactory
                .select(cafe)
                .from(planCafe)
                .leftJoin(planCafe.cafe, cafe)
                .where(planIdEq(planId),
                        matchTypeEq(matchType))
                .fetch();
    }

    @Override
    public List<Cafe> findNearestCafes(final BigDecimal latitude,
                                       final BigDecimal longitude) {

        NumberExpression<Double> distance =
                Expressions.numberTemplate(Double.class,
                        "6371 * acos(cos(radians({0})) * cos(radians({1})) * cos(radians({2}) - radians({3})) " +
                                "+ sin(radians({0})) * sin(radians({1})))",
                        latitude.doubleValue(), cafe.latitude.doubleValue(), cafe.longitude.doubleValue(), longitude.doubleValue());

        return queryFactory
                .selectFrom(cafe)
                .orderBy(distance.asc())
                .limit(10)
                .fetch();
    }

    @Override
    public List<Cafe> findByDate(final LocalDate date,
                                 final Sort sort,
                                 final Integer limit) {

        BooleanBuilder conditions = new BooleanBuilder();
        conditions.and(dateEq(date));
        NumberPath<BigDecimal> starRatingPath = cafe.starRating;
        List<OrderSpecifier<?>> orderSpecifiers = QuerydslUtil.getOrderSpecifiers(sort, starRatingPath);

        return queryFactory
                .select(cafe)
                .from(cafe)
                .leftJoin(cafe.operationHours, operationHour)
                .where(conditions)
                .orderBy(cafe.starRating.desc())
                .limit(limit)
                .fetch();
    }

    private BooleanExpression matchTypeEq(final MatchType matchType) {
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

    private BooleanExpression isWithinRadius(final BigDecimal latitude,
                                             final BigDecimal longitude,
                                             final Integer minutes) {

        if(isEmpty(latitude) || isEmpty(longitude)) return null;

        double radiusInKm;
        if(isEmpty(minutes) || minutes == 0) radiusInKm = calculateRadiusInKm(MAX_RADIUS_TIME);
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
