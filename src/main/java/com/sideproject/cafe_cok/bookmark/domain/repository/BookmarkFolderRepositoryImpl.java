package com.sideproject.cafe_cok.bookmark.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderCountDto;
import com.sideproject.cafe_cok.bookmark.dto.QBookmarkFolderCountDto;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.sideproject.cafe_cok.bookmark.domain.QBookmarkFolder.*;
public class BookmarkFolderRepositoryImpl implements BookmarkFolderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BookmarkFolderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<BookmarkFolderCountDto> findBookmarkFolderCountDtoListByMemberId(final Long memberId) {

        return queryFactory
                .select(new QBookmarkFolderCountDto(
                        bookmarkFolder.id,
                        bookmarkFolder.name,
                        bookmarkFolder.color,
                        bookmarkFolder.isVisible,
                        bookmarkFolder.isDefaultFolder,
                        bookmarkFolder.bookmarks.size()))
                .from(bookmarkFolder)
                .where(bookmarkFolder.member.id.eq(memberId))
                .fetch();
    }

}
