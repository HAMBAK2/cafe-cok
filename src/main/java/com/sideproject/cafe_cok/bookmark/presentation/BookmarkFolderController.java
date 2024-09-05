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
@RequestMapping("/api/v1/bookmark-folders")
@Tag(name = "bookmark-folders", description = "북마크 폴더 API")
public class BookmarkFolderController {

    private final BookmarkFolderService bookmarkFolderService;
    private final HttpHeadersUtil httpHeadersUtil;

    @GetMapping
    @Operation(summary = "북마크 폴더 목록 조회")
    public ResponseEntity<BookmarkFoldersResponse> findList(@AuthenticationPrincipal LoginMember loginMember) {
        BookmarkFoldersResponse response = bookmarkFolderService.bookmarkFolders(loginMember);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("bookmark-folders/findList");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "북마크 폴더 저장")
    public ResponseEntity<Void> save(@AuthenticationPrincipal LoginMember loginMember,
                                     @RequestBody BookmarkFolderSaveRequest request) {

        bookmarkFolderService.save(request, loginMember);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("bookmark-folders/save");
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @PutMapping
    @Operation(summary = "북마크 폴더 수정")
    public ResponseEntity<Void> update(@AuthenticationPrincipal LoginMember loginMember,
                                       @RequestBody BookmarkFolderUpdateRequest request) {

        bookmarkFolderService.update(request);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("bookmark-folders/update");
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{folderId}")
    @Operation(summary = "folderId에 해당하는 북마크 폴더의 지도 노출 여부 수정")
    public ResponseEntity<Void> updateVisible(@AuthenticationPrincipal LoginMember loginMember,
                                              @PathVariable Long folderId){

        bookmarkFolderService.updateFolderVisible(folderId);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("bookmark-folders/updateVisible");
        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{folderId}")
    @Operation(summary = "folderId에 해당하는 북마크 폴더 삭제")
    public ResponseEntity<BookmarkFolderDeleteResponse> delete(@AuthenticationPrincipal LoginMember loginMember,
                                                               @PathVariable Long folderId){

        BookmarkFolderDeleteResponse response = bookmarkFolderService.delete(folderId);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("bookmark-folders/delete");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @GetMapping("/{folderId}")
    @Operation(summary = "folderId에 해당하는 북마크 폴더 조회")
    public ResponseEntity<BookmarksResponse> find(@AuthenticationPrincipal LoginMember loginMember,
                                                  @PathVariable Long folderId) {

        BookmarksResponse response = bookmarkFolderService.bookmarks(folderId);
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("bookmark-folders/find");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }


}
