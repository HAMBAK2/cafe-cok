package com.sideproject.cafe_cok.admin.dto.request;

import com.sideproject.cafe_cok.admin.dto.AdminImageDto;
import com.sideproject.cafe_cok.cafe.dto.CafeOperationHourDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "카페 수정 요청")
public class AdminCafeUpdateRequest {

    @Schema(description = "카페 ID", example = "1")
    private Long id;

    @Schema(description = "카페 이름", example = "카페 이름")
    private String name;

    @Schema(description = "카페 도로명 주소", example = "서울 종로구 종로5길 7")
    private String address;

    @Schema(description = "카페 전화번호", example = "000-0000-0000")
    private String phoneNumber;

    @Schema(description = "카페 메인 이미지 정보")
    private AdminImageDto image;

    @Schema(description = "카페 나머지 이미지 정보 리스트")
    private List<AdminImageDto> otherImages;

    @Schema(description = "카페 메뉴 리스트")
    private List<AdminMenuRequestDto> menus;

    @Schema(description = "카페 운영시간 리스트")
    private List<CafeOperationHourDto> hours;
}
