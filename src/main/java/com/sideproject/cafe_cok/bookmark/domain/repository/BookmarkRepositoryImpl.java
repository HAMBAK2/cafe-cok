package com.sideproject.cafe_cok.bookmark.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.bookmark.domain.QBookmark;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkIdDto;
import com.sideproject.cafe_cok.bookmark.dto.QBookmarkIdDto;
import jakarta.persistence.EntityManager;


import java.util.ArrayList;
import java.util.List;

import static com.sideproject.cafe_cok.bookmark.domain.QBookmark.*;

public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public BookmarkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<BookmarkIdDto> findBookmarkIdDtoListByCafeIdAndMemberId(final Long cafeId,
                                                                        final Long memberId) {

        return queryFactory
                .select(new QBookmarkIdDto(
                        bookmark.id,
                        bookmark.bookmarkFolder.id
                ))
                .from(bookmark)
                .where(bookmark.cafe.id.eq(cafeId),
                        bookmark.bookmarkFolder.member.id.eq(memberId))
                .fetch();
    }
}
