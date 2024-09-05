package com.sideproject.cafe_cok.plan.domain.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.keword.domain.enums.Category;
import com.sideproject.cafe_cok.plan.domain.Plan;
import com.sideproject.cafe_cok.plan.domain.condition.PlanSearchCondition;
import com.sideproject.cafe_cok.plan.domain.enums.PlanSortBy;
import com.sideproject.cafe_cok.plan.domain.enums.PlanStatus;
import com.sideproject.cafe_cok.plan.dto.PlanKeywordDto;
import com.sideproject.cafe_cok.plan.dto.QPlanKeywordDto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.sideproject.cafe_cok.keword.domain.QKeyword.*;
import static com.sideproject.cafe_cok.member.domain.QMember.*;
import static com.sideproject.cafe_cok.plan.domain.QPlan.*;
import static com.sideproject.cafe_cok.plan.domain.QPlanKeyword.*;
import static org.springframework.util.StringUtils.isEmpty;

public class PlanRepositoryImpl implements PlanRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public PlanRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<PlanKeywordDto> findPlanKeywordDtoList(final PlanSearchCondition searchCondition,
                                                       final Pageable pageable) {

        JPAQuery<PlanKeywordDto> query = queryFactory
                .select(new QPlanKeywordDto(
                        plan.id,
                        plan.locationName,
                        plan.visitDate,
                        plan.visitStartTime,
                        keyword.name))
                .from(plan)
                .leftJoin(plan.member, member)
                .leftJoin(plan.planKeywords, planKeyword)
                .leftJoin(planKeyword.keyword, keyword)
                .where(memberIdEq(searchCondition.getMemberId()),
                        categoryEq(searchCondition.getCategory()),
                        statusIsTrue(searchCondition.getStatus()),
                        planSortByFiltered(searchCondition.getPlanSortBy()))
                .groupBy(plan.id)
                .having(plan.id.count().eq(1L))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<? extends Plan> pathBuilder = new PathBuilder<>(plan.getType(), plan.getMetadata());
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            query.orderBy(new OrderSpecifier(direction, pathBuilder.get(property)));
        }

        return query.fetch();
    }

    private BooleanExpression memberIdEq(final Long memberId) {
        return isEmpty(memberId) ? null : member.id.eq(memberId);
    }

    private BooleanExpression categoryEq(final Category category) {
        return isEmpty(category) ? null : keyword.category.eq(category);
    }

    private BooleanExpression statusIsTrue(final PlanStatus status) {
        return status.equals(PlanStatus.SAVED) ? plan.isSaved.eq(true) : plan.isShared.eq(true);
    }

    private BooleanExpression planSortByFiltered(final PlanSortBy planSortBy) {

        if(planSortBy.equals(PlanSortBy.RECENT)) return null;
        return plan.visitDate.goe(LocalDate.now())
                .and(plan.visitStartTime.isNull()
                        .or(plan.visitStartTime.goe(LocalTime.now())));
    }
}
