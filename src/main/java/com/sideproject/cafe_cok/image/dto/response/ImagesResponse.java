package com.sideproject.cafe_cok.image.dto.response;

import com.sideproject.cafe_cok.image.dto.ImageUrlDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
public class ImagesResponse extends RepresentationModel<ImagesResponse> {

    private List<ImageUrlDto> imageUrls;

    public ImagesResponse(final List<ImageUrlDto> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
