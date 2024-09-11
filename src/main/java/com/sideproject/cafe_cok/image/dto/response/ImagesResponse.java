package com.sideproject.cafe_cok.image.dto.response;

import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "카페 이미지 조회 응답")
public class ImagesResponse extends RepresentationModel<ImagesResponse> {

    @Schema(description = "카페 이미지 리스트")
    private List<ImageUrlDto> imageUrls;

    public ImagesResponse(final List<ImageUrlDto> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
