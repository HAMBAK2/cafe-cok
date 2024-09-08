package com.sideproject.cafe_cok.bookmark.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sideproject.cafe_cok.bookmark.dto.BookmarkFolderDetailDto;
import com.sideproject.cafe_cok.bookmark.dto.QBookmarkFolderDetailDto;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkFolderUpdateRequest;
import com.sideproject.cafe_cok.bookmark.exception.DefaultFolderUpdateNotAllowedException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

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
                .groupBy(bookmarkFolder)
                .fetch();
    }

    @Override
    public void update(final BookmarkFolderUpdateRequest request) {

        Boolean isDefaultFolder = queryFactory
                .select(bookmarkFolder.isDefaultFolder)
                .from(bookmarkFolder)
                .where(bookmarkFolder.id.eq(request.getFolderId()))
                .fetchOne();

        if(isDefaultFolder == null)
            throw new EntityNotFoundException("[ID : " + request.getFolderId() + "] 에 해당하는 북마크가 존재하지 않습니다.");

        if(Boolean.TRUE.equals(isDefaultFolder))
            throw new DefaultFolderUpdateNotAllowedException();

        queryFactory.update(bookmarkFolder)
                .set(bookmarkFolder.name, request.getName())
                .set(bookmarkFolder.color, request.getColor())
                .set(bookmarkFolder.isVisible, request.getIsVisible())
                .where(bookmarkFolder.id.eq(request.getFolderId()))
                .execute();
    }

    @Override
    public void toggleFolderVisibility(final Long folderId) {

        long updatedCount = queryFactory.update(bookmarkFolder)
                .set(bookmarkFolder.isVisible, bookmarkFolder.isVisible.not())
                .where(bookmarkFolder.id.eq(folderId))
                .execute();
        if(updatedCount == 0)
            throw new EntityNotFoundException("[ID : " + folderId + "] 에 해당하는 북마크가 존재하지 않습니다.");
    }

}
