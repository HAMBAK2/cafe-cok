package com.sideproject.cafe_cok.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberPath;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class QuerydslUtil {

    public static OrderSpecifier<?> getOrderSpecifier(Sort.Order order, NumberPath<?> path) {
        Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
        return new OrderSpecifier<>(direction, path);
    }

    public static List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort,
                                                             NumberPath<BigDecimal> path) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (!sort.isEmpty()) {
            for (Sort.Order order : sort) {
                OrderSpecifier<?> orderSpecifier = getOrderSpecifier(order, path);
                orderSpecifiers.add(orderSpecifier);
            }
        }

        return orderSpecifiers;
    }

    public static List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable,
                                                             NumberPath<Long> path) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                OrderSpecifier<?> orderSpecifier = getOrderSpecifier(order, path);
                orderSpecifiers.add(orderSpecifier);
            }
        }

        return orderSpecifiers;
    }

}

