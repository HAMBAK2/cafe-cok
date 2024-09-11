package com.sideproject.cafe_cok.admin.dto.request;


import com.sideproject.cafe_cok.cafe.dto.CafeOperationHourDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@Schema(description = "카페 저장 요청")
public class AdminCafeSaveRequest {

    @Schema(description = "카페 이름", example = "카페 이름")
    private String name;

    @Schema(description = "카페 도로명 주소", example = "서울 종로구 종로5길 7")
    private String address;

    @Schema(description = "카페 전화번호", example = "000-0000-0000")
    private String phone;

    @Schema(description = "카페 위도", example = "37.57061772252790")
    private BigDecimal latitude;

    @Schema(description = "카페 경도", example = "126.98055287409800")
    private BigDecimal longitude;

    @Schema(description = "카카오 MAP API에 저장된 장소의 ID", example = "12346")
    private Long kakaoId;

    @Schema(description = "카페 메인 이미지 Base64", example = "카페 메인 이미지 Base64")
    private String mainImage;

    @Schema(description = "카페 나머지 이미지 Base64 리스트", example = "카페 나머지 이미지 Base64 리스트")
    private List<String> otherImages;

    @Schema(description = "저장하려는 카페 메뉴 정보 리스트")
    private List<AdminMenuRequestDto> menus;

    @Schema(description = "저장하려는 카페 운영 시간 리스트")
    private List<CafeOperationHourDto> hours;
}
