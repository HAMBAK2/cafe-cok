package com.sideproject.cafe_cok.bookmark.presentation;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkSaveRequest;
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
@Tag(name = "Bookmark", description = "북마크 관련 API")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/save")
    @Operation(summary = "저장버튼 클릭 -> 폴더 선택 후 저장 완료 시 동작하는 기능")
    public ResponseEntity<BookmarkIdResponse> saveBookmark(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody BookmarkSaveRequest request) {

        BookmarkIdResponse response = bookmarkService.save(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{bookmarkId}/delete")
    @Operation(summary = "저장된 북마크 삭제 기능")
    public ResponseEntity<BookmarkIdResponse> deleteBookmark(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long bookmarkId) {

        BookmarkIdResponse response = bookmarkService.delete(bookmarkId);
        return ResponseEntity.ok(response);
    }
}
