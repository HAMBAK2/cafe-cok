package com.sideproject.cafe_cok.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "이미지 정보 DTO")
public class AdminImageDto {

    @Schema(description = "카페 ID", example = "1")
    private Long id;

    @Schema(description = "이미지 base64", example = "이미지 base64")
    private String imageBase64;
}
