package com.sideproject.cafe_cok.cafe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Getter
@NoArgsConstructor
@Schema(description = "카페 저장/수정 응답")
public class CafeSaveResponse extends RepresentationModel<CafeSaveResponse> {

    @Schema(description = "카페 저장 성공 메시지", example = "Save/Update successful")
    private String message;

    @Schema(description = "카페 저장 후 리다이렉트 할 URL", example = "/리다이렉트_URL")
    private String redirectUrl;

    public CafeSaveResponse(final String message,
                            final String redirectUrl) {
        this.message = message;
        this.redirectUrl = redirectUrl;
    }
}
