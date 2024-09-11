package com.sideproject.cafe_cok.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "카페에 저장하려는 메뉴 DTO")
public class AdminMenuRequestDto {

    @Schema(description = "카페 ID", example = "1")
    private Long id;

    @Schema(description = "메뉴 이름", example = "아메리카노")
    private String name;

    @Schema(description = "메뉴 가격", example = "5000")
    private Integer price;

    @Schema(description = "메뉴 이미지 Base64", example = "메뉴 이미지 Base64")
    private String image;
}
