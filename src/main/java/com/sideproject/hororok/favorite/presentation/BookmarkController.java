package com.sideproject.hororok.favorite.presentation;


import com.sideproject.hororok.auth.dto.LoginMember;
import com.sideproject.hororok.auth.presentation.AuthenticationPrincipal;
import com.sideproject.hororok.favorite.application.BookmarkFolderService;
import com.sideproject.hororok.favorite.dto.response.BookmarkFoldersResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmark")
@Tag(name = "Favorite", description = "즐겨찾기(북마크) 관련 API")
public class BookmarkController {

    private final BookmarkFolderService bookmarkFolderService;
    
    @GetMapping("/folders")
    @Operation(summary = "하단 탭의 \"저장\" 버튼을 눌렀을 때 필요한 정보 제공")
    public ResponseEntity<BookmarkFoldersResponse> myPlace(@AuthenticationPrincipal LoginMember loginMember) {
        BookmarkFoldersResponse response = bookmarkFolderService.bookmarkFolders(loginMember);
        return ResponseEntity.ok(response);
    }
}
