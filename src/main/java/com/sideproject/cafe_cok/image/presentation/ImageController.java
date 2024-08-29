package com.sideproject.cafe_cok.image.presentation;

import com.sideproject.cafe_cok.image.dto.response.ImageAllResponse;
import com.sideproject.cafe_cok.image.dto.response.ImagePageResponse;
import com.sideproject.cafe_cok.image.application.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
@Tag(name = "Image", description = "이미지 API")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/by-cafe/{cafeId}")
    @Operation(summary = "cafeId에 해당하는 이미지 조회(페이징)")
    public ResponseEntity<ImagePageResponse> findByCafeId(@PathVariable Long cafeId,
                                                          @RequestParam(required = false) Long cursor) {

        ImagePageResponse response = imageService.findByCafeId(cafeId, cursor);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-cafe/{cafeId}/all")
    @Operation(summary = "cafeId에 해당하는 이미지 조회(전체)")
    public ResponseEntity<ImageAllResponse> detailImagesAll(@PathVariable Long cafeId) {

        ImageAllResponse response = imageService.findByCafeIdAll(cafeId);
        return ResponseEntity.ok(response);
    }
}
