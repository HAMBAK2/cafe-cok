package com.sideproject.cafe_cok.bookmark.presentation;


import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkFolderDeleteResponse;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkFoldersResponse;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarksResponse;
import com.sideproject.cafe_cok.bookmark.application.BookmarkFolderService;
import com.sideproject.cafe_cok.bookmark.application.BookmarkService;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkFolderSaveRequest;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkFolderUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmark-folders")
@Tag(name = "Bookmark Folder", description = "북마크 폴더 API")
public class BookmarkFolderController {

    private final BookmarkFolderService bookmarkFolderService;

    @GetMapping
    @Operation(summary = "북마크 폴더 조회")
    public ResponseEntity<BookmarkFoldersResponse> bookmarkFolders(
            @AuthenticationPrincipal LoginMember loginMember) {
        BookmarkFoldersResponse response = bookmarkFolderService.bookmarkFolders(loginMember);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "북마크 폴더 저장")
    public ResponseEntity<Void> saveFolder(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody BookmarkFolderSaveRequest request) {

        bookmarkFolderService.save(request, loginMember);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Operation(summary = "북마크 폴더 수정")
    public ResponseEntity<Void> updateFolder(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody BookmarkFolderUpdateRequest request) {

        bookmarkFolderService.update(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{folderId}")
    @Operation(summary = "folderId에 해당하는 북마크 폴더의 지도 노출 여부 수정")
    public ResponseEntity<Void> updateFolderVisible(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long folderId){

        bookmarkFolderService.updateFolderVisible(folderId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{folderId}")
    @Operation(summary = "folderId에 해당하는 북마크 폴더 삭제")
    public ResponseEntity<BookmarkFolderDeleteResponse> deleteFolder(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long folderId){

        BookmarkFolderDeleteResponse response = bookmarkFolderService.delete(folderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{folderId}")
    @Operation(summary = "folderId에 해당하는 북마크 폴더 조회")
    public ResponseEntity<BookmarksResponse> bookmarks(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long folderId) {

        BookmarksResponse response = bookmarkFolderService.bookmarks(folderId);
        return ResponseEntity.ok(response);
    }


}
