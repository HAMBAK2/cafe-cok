package com.sideproject.cafe_cok.image.dto.response;

import com.sideproject.cafe_cok.cafe.dto.response.CafeTopResponse;
import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Builder
public class ImagesResponse extends RepresentationModel<CafeTopResponse> {

    private List<ImageUrlDto> imageUrls;

    public static ImagesResponse from(final List<ImageUrlDto> imageUrls) {
        return ImagesResponse.builder()
                .imageUrls(imageUrls)
                .build();
    }

}
