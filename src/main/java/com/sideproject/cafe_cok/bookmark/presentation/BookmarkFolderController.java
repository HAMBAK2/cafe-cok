package com.sideproject.cafe_cok.bookmark.presentation;


import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkFolderIdResponse;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkFoldersResponse;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarksResponse;
import com.sideproject.cafe_cok.bookmark.application.BookmarkFolderService;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkFolderSaveRequest;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkFolderUpdateRequest;
import com.sideproject.cafe_cok.util.HttpHeadersUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmark-folders")
@Tag(name = "bookmark-folders", description = "북마크 폴더 API")
@ApiResponse(responseCode = "200", description = "성공")
public class BookmarkFolderController {

    private final BookmarkFolderService bookmarkFolderService;
    private final HttpHeadersUtil httpHeadersUtil;

    @GetMapping
    @Operation(summary = "북마크 폴더 목록 조회")
    public ResponseEntity<BookmarkFoldersResponse> findList(@AuthenticationPrincipal LoginMember loginMember) {
        BookmarkFoldersResponse response = bookmarkFolderService.bookmarkFolders(loginMember);
        response.add(linkTo(methodOn(BookmarkFolderController.class).findList(loginMember)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(BookmarkFolderController.class).detail(loginMember, null)).withRel("detail").withType("GET"))
                .add(linkTo(methodOn(BookmarkFolderController.class).save(loginMember, null)).withRel("save").withType("POST"))
                .add(linkTo(methodOn(BookmarkFolderController.class).update(loginMember, null)).withRel("update").withType("PUT"))
                .add(linkTo(methodOn(BookmarkFolderController.class).updateVisible(loginMember, null)).withRel("update").withType("PATCH"));

        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("bookmark-folders/findList");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "북마크 폴더 저장")
    public ResponseEntity<BookmarkFolderIdResponse> save(@AuthenticationPrincipal LoginMember loginMember,
                                                         @RequestBody BookmarkFolderSaveRequest request) {

        BookmarkFolderIdResponse response = bookmarkFolderService.save(request, loginMember);
        response.add(linkTo(methodOn(BookmarkFolderController.class).save(loginMember, request)).withSelfRel().withType("POST"));
        response.add(linkTo(methodOn(BookmarkFolderController.class).findList(loginMember)).withRel("list").withType("GET"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("bookmark-folders/save");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "북마크 폴더 수정")
    public ResponseEntity<BookmarkFolderIdResponse> update(@AuthenticationPrincipal LoginMember loginMember,
                                                           @RequestBody BookmarkFolderUpdateRequest request) {

        BookmarkFolderIdResponse response = bookmarkFolderService.update(request);
        response.add(linkTo(methodOn(BookmarkFolderController.class).update(loginMember, request)).withSelfRel().withType("PUT"));
        response.add(linkTo(methodOn(BookmarkFolderController.class).findList(loginMember)).withRel("list").withType("GET"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("bookmark-folders/update");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PatchMapping("/{folderId}")
    @Operation(summary = "folderId에 해당하는 북마크 폴더의 지도 노출 여부 수정")
    @Parameter(name = "folderId", description = "수정하려는 폴더의 ID", example = "1")
    public ResponseEntity<BookmarkFolderIdResponse> updateVisible(@AuthenticationPrincipal LoginMember loginMember,
                                                                  @PathVariable Long folderId){

        BookmarkFolderIdResponse response = bookmarkFolderService.updateFolderVisible(folderId);
        response.add(linkTo(methodOn(BookmarkFolderController.class).updateVisible(loginMember, folderId)).withSelfRel().withType("PATCH"))
                .add(linkTo(methodOn(BookmarkFolderController.class).findList(null)).withRel("list").withType("GET"))
                .add(linkTo(methodOn(BookmarkFolderController.class).detail(null, null)).withRel("detail").withType("GET"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("bookmark-folders/updateVisible");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{folderId}")
    @Operation(summary = "folderId에 해당하는 북마크 폴더 삭제")
    @Parameter(name = "folderId", description = "삭제하려는 폴더의 ID", example = "1")
    public ResponseEntity<BookmarkFolderIdResponse> delete(@AuthenticationPrincipal LoginMember loginMember,
                                                           @PathVariable Long folderId){

        BookmarkFolderIdResponse response = bookmarkFolderService.delete(folderId);
        response.add(linkTo(methodOn(BookmarkFolderController.class).delete(loginMember, folderId)).withSelfRel().withType("DELETE"));
        response.add(linkTo(methodOn(BookmarkFolderController.class).findList(loginMember)).withRel("list").withType("GET"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("bookmark-folders/delete");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @GetMapping("/{folderId}")
    @Operation(summary = "folderId에 해당하는 북마크 폴더 조회")
    @Parameter(name = "folderId", description = "조회하려는 폴더의 ID", example = "1")
    public ResponseEntity<BookmarksResponse> detail(@AuthenticationPrincipal LoginMember loginMember,
                                                    @PathVariable Long folderId) {

        BookmarksResponse response = bookmarkFolderService.find(folderId);
        response.add(linkTo(methodOn(BookmarkFolderController.class).detail(loginMember, folderId)).withSelfRel().withType("GET"))
                .add(linkTo(methodOn(BookmarkFolderController.class).update(loginMember, null)).withRel("update").withType("PUT"))
                .add(linkTo(methodOn(BookmarkFolderController.class).delete(loginMember, null)).withRel("delete").withType("DELETE"))
                .add(linkTo(methodOn(BookmarkController.class).delete(loginMember, null)).withRel("delete").withType("DELETE"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("bookmark-folders/detail");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }


}
