package com.sideproject.cafe_cok.bookmark.presentation;

import com.sideproject.cafe_cok.auth.dto.LoginMember;
import com.sideproject.cafe_cok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.cafe_cok.bookmark.dto.request.BookmarkSaveRequest;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkFolderAndBookmarkIdResponse;
import com.sideproject.cafe_cok.bookmark.dto.response.BookmarkIdResponse;
import com.sideproject.cafe_cok.bookmark.application.BookmarkService;
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
@RequestMapping("/api/v1/bookmarks")
@Tag(name = "bookmarks", description = "Bookmark API")
@ApiResponse(responseCode = "200", description = "성공")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final HttpHeadersUtil httpHeadersUtil;

    @PostMapping
    @Operation(summary = "북마크 저장")
    public ResponseEntity<BookmarkFolderAndBookmarkIdResponse> save(@AuthenticationPrincipal LoginMember loginMember,
                                                                    @RequestBody BookmarkSaveRequest request) {

        BookmarkFolderAndBookmarkIdResponse response = bookmarkService.save(request);
        response.add(linkTo(methodOn(BookmarkController.class).save(loginMember, request)).withSelfRel().withType("POST"))
                .add(linkTo(methodOn(BookmarkController.class).delete(null, null)).withRel("delete").withType("DELETE"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("bookmarks/save");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{bookmarkId}")
    @Operation(summary = "북마크 삭제")
    @Parameter(name = "bookmarkId", description = "삭제하려는 북마크의 ID", example = "123")
    public ResponseEntity<BookmarkFolderAndBookmarkIdResponse> delete(@AuthenticationPrincipal LoginMember loginMember,
                                                                      @PathVariable Long bookmarkId) {

        BookmarkFolderAndBookmarkIdResponse response = bookmarkService.delete(bookmarkId);
        response.add(linkTo(methodOn(BookmarkController.class).delete(loginMember, bookmarkId)).withSelfRel().withType("DELETE"))
                .add(linkTo(methodOn(BookmarkController.class).save(null, null)).withRel("save").withType("POST"));
        HttpHeaders headers = httpHeadersUtil.createLinkHeaders("bookmarks/delete");
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
