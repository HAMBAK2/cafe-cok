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
@RequestMapping("/api/bookmark")
@Tag(name = "Bookmark Folder", description = "북마크 폴더 관련 API")
public class BookmarkFolderController {

    private final BookmarkService bookmarkService;
    private final BookmarkFolderService bookmarkFolderService;

    @GetMapping("/folders")
    @Operation(summary = "하단 탭의 \"저장\" 버튼을 눌렀을 때 필요한 정보 제공")
    public ResponseEntity<BookmarkFoldersResponse> bookmarkFolders(
            @AuthenticationPrincipal LoginMember loginMember) {
        BookmarkFoldersResponse response = bookmarkFolderService.bookmarkFolders(loginMember);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/folder/save")
    @Operation(summary = "새로운 북마크 폴더를 생성할 때 동작하는 기능")
    public ResponseEntity<Void> saveFolder(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody BookmarkFolderSaveRequest request) {

        bookmarkFolderService.save(request, loginMember);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/folder/update")
    @Operation(summary = "북마크 폴더 수정 시 동작하는 기능")
    public ResponseEntity<Void> updateFolder(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody BookmarkFolderUpdateRequest request) {

        bookmarkFolderService.update(request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/folder/{folderId}/update/visible")
    @Operation(summary = "폴더의  토글(지도 노출 여부) 버튼을 눌렀을 때 동작하는 기능")
    public ResponseEntity<Void> updateFolderVisible(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long folderId){

        bookmarkFolderService.updateFolderVisible(folderId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/folder/{folderId}/delete")
    @Operation(summary = "폴더 삭제 버튼을 눌렀을 때 동작하는 기능")
    public ResponseEntity<BookmarkFolderDeleteResponse> deleteFolder(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long folderId){

        BookmarkFolderDeleteResponse response = bookmarkFolderService.delete(folderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/folder/{folderId}")
    @Operation(summary = "북마크 폴더를 선택 시 해당 폴더 북마크 리스트를 조회하는 기능")
    public ResponseEntity<BookmarksResponse> bookmarks(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long folderId) {

        BookmarksResponse response = bookmarkService.bookmarks(folderId);
        return ResponseEntity.ok(response);
    }


}
