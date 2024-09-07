package com.sideproject.cafe_cok.bookmark.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderDetailDto;
import com.sideproject.cafe_cok.bookmark.dto.QBookmarkFolderDetailDto;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.sideproject.cafe_cok.bookmark.domain.QBookmark.*;
import static com.sideproject.cafe_cok.bookmark.domain.QBookmarkFolder.*;
public class BookmarkFolderRepositoryImpl implements BookmarkFolderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BookmarkFolderRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<BookmarkFolderDetailDto> getBookmarkFolderDetails(final Long memberId) {

        return queryFactory
                .select(new QBookmarkFolderDetailDto(
                        bookmarkFolder.id,
                        bookmarkFolder.name,
                        bookmarkFolder.color,
                        bookmarkFolder.isVisible,
                        bookmarkFolder.isDefaultFolder,
                        bookmark.count()))
                .from(bookmarkFolder)
                .leftJoin(bookmarkFolder.bookmarks, bookmark)
                .where(bookmarkFolder.member.id.eq(memberId))
                .fetch();
    }

}
