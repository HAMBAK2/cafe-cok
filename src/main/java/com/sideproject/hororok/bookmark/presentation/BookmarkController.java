package com.sideproject.hororok.bookmark.presentation;

import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.hororok.bookmark.application.BookmarkFolderService;
import com.sideproject.hororok.bookmark.application.BookmarkService;
import com.sideproject.hororok.bookmark.dto.request.BookmarkSaveRequest;
import com.sideproject.hororok.bookmark.dto.response.BookmarkFoldersResponse;
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
    public ResponseEntity<Void> saveBookmark(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody BookmarkSaveRequest request) {

        bookmarkService.save(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{bookmarkId}/delete")
    @Operation(summary = "저장된 북마크 삭제 기능")
    public ResponseEntity<Void> deleteBookmark(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long bookmarkId) {

        bookmarkService.delete(bookmarkId);
        return ResponseEntity.noContent().build();
    }


}
