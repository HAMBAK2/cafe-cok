package com.sideproject.hororok.favorite.presentation;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.hororok.favorite.application.BookmarkFolderService;
import com.sideproject.hororok.favorite.application.BookmarkService;
import com.sideproject.hororok.favorite.dto.request.BookmarkSaveRequest;
import com.sideproject.hororok.favorite.dto.response.BookmarkFoldersResponse;
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
    private final BookmarkFolderService bookmarkFolderService;

    @GetMapping("/save")
    @Operation(summary = "북마크 저장 버튼 클릭 시 동작, 폴더 목록을 반환 받는다.")
    public ResponseEntity<BookmarkFoldersResponse> saveBookmark(
            @AuthenticationPrincipal LoginMember loginMember) {

        BookmarkFoldersResponse response = bookmarkFolderService.bookmarkFolders(loginMember);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    @Operation(summary = "저장버튼 클릭 -> 폴더 선택 후 저장 완료 시 동작하는 기능")
    public ResponseEntity<Void> saveBookmark(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody BookmarkSaveRequest request) {

        bookmarkService.save(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{bookmarkId}")
    @Operation(summary = "저장된 북마크 삭제 기능")
    public ResponseEntity<Void> deleteBookmark(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long bookmarkId) {

        bookmarkService.delete(bookmarkId);
        return ResponseEntity.noContent().build();
    }


}
