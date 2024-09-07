package com.sideproject.cafe_cok.bookmark.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderIdsDto;
import com.sideproject.cafe_cok.bookmark.dto.QBookmarkFolderIdsDto;
import jakarta.persistence.EntityManager;


import java.util.List;

import static com.sideproject.cafe_cok.bookmark.domain.QBookmark.*;

public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public BookmarkRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<BookmarkFolderIdsDto> getBookmarkFolderIds(final Long cafeId,
                                                           final Long memberId) {

        return queryFactory
                .select(new QBookmarkFolderIdsDto(
                        bookmark.id,
                        bookmark.bookmarkFolder.id
                ))
                .from(bookmark)
                .where(bookmark.cafe.id.eq(cafeId),
                        bookmark.bookmarkFolder.member.id.eq(memberId))
                .fetch();
    }
}
