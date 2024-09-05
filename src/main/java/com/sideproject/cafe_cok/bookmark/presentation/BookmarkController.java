package com.sideproject.cafe_cok.bookmark.presentation;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkSaveRequest;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkFolderAndBookmarkIdResponse;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkIdResponse;
import com.sideproject.cafe_cok.bookmark.application.BookmarkService;
import com.sideproject.cafe_cok.util.HttpHeadersUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmarks")
@Tag(name = "bookmarks", description = "Bookmark API")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final HttpHeadersUtil httpHeadersUtil;

    @PostMapping
    @Operation(summary = "북마크 저장")
    public ResponseEntity<BookmarkFolderAndBookmarkIdResponse> save(@AuthenticationPrincipal LoginMember loginMember,
                                                                    @RequestBody BookmarkSaveRequest request) {

        BookmarkFolderAndBookmarkIdResponse response = bookmarkService.save(request);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("bookmarks/save");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{bookmarkId}")
    @Operation(summary = "북마크 삭제")
    public ResponseEntity<BookmarkFolderAndBookmarkIdResponse> deleteBookmark(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long bookmarkId) {

        BookmarkFolderAndBookmarkIdResponse response = bookmarkService.delete(bookmarkId);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("bookmarks/delete");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
