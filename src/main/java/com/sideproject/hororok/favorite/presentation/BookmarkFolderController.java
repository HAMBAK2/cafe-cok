package com.sideproject.hororok.favorite.presentation;


import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.hororok.favorite.application.BookmarkFolderService;
import com.sideproject.hororok.favorite.application.BookmarkService;
import com.sideproject.hororok.favorite.domain.BookmarkFolder;
import com.sideproject.hororok.favorite.dto.request.BookmarkFolderSaveRequest;
import com.sideproject.hororok.favorite.dto.request.BookmarkFolderUpdateRequest;
import com.sideproject.hororok.favorite.dto.request.BookmarkSaveRequest;
import com.sideproject.hororok.favorite.dto.response.BookmarkFoldersResponse;
import com.sideproject.hororok.favorite.dto.response.BookmarksResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark")
@Tag(name = "Favorite", description = "즐겨찾기(북마크) 관련 API")
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

    @GetMapping("/folders")
    @Operation(summary = "하단 탭의 \"저장\" 버튼을 눌렀을 때 필요한 정보 제공")
    public ResponseEntity<BookmarkFoldersResponse> bookmarkFolders(
            @AuthenticationPrincipal LoginMember loginMember) {
        BookmarkFoldersResponse response = bookmarkFolderService.bookmarkFolders(loginMember);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/folder/{folderId}")
    @Operation(summary = "북마크 폴더를 선택 시 동작하는 기능")
    public ResponseEntity<BookmarksResponse> bookmarks(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long folderId) {

        BookmarksResponse response = bookmarkService.bookmarks(folderId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/folder/save")
    @Operation(summary = "북마크 -> 새 폴더 추가 -> 완료 선택 시 기능")
    public ResponseEntity<BookmarkFoldersResponse> saveFolder(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody BookmarkFolderSaveRequest request) {

        BookmarkFoldersResponse response = bookmarkFolderService.save(request, loginMember);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/folder/update")
    @Operation(summary = "'편집하기' -> '수정' -> '완료' 선택 시 기능")
    public ResponseEntity<BookmarkFoldersResponse> updateFolder(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody BookmarkFolderUpdateRequest request) {

        BookmarkFoldersResponse response = bookmarkFolderService.update(request, loginMember);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/folder/{folderId}/update/visible")
    @Operation(summary = "폴더의  토글(지도 노출 여부) 버튼을 눌렀을 때 동작하는 기능")
    public ResponseEntity<Void> updateFolderVisible(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long folderId){

        bookmarkFolderService.updateFolderVisible(folderId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/folder/{folderId}/delete")
    @Operation(summary = "폴더 삭제 버튼을 눌렀을 때 동작하는 기능")
    public ResponseEntity<BookmarkFoldersResponse> deleteFolder(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long folderId){

        BookmarkFoldersResponse response
                = bookmarkFolderService.delete(folderId, loginMember);
        return ResponseEntity.ok(response);
    }
}
