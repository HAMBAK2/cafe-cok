package com.sideproject.cafe_cok.bookmark.presentation;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkSaveRequest;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkFolderAndBookmarkIdResponse;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkIdResponse;
import com.sideproject.cafe_cok.bookmark.application.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark")
@Tag(name = "Bookmark", description = "Bookmark API")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping
    @Operation(summary = "북마크 저장")
    public ResponseEntity<BookmarkFolderAndBookmarkIdResponse> saveBookmark(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody BookmarkSaveRequest request) {

        BookmarkFolderAndBookmarkIdResponse response = bookmarkService.save(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{bookmarkId}")
    @Operation(summary = "북마크 삭제")
    public ResponseEntity<BookmarkFolderAndBookmarkIdResponse> deleteBookmark(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long bookmarkId) {

        BookmarkFolderAndBookmarkIdResponse response = bookmarkService.delete(bookmarkId);
        return ResponseEntity.ok(response);
    }
}
