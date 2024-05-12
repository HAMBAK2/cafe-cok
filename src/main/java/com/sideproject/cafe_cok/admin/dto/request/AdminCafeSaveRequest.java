package com.sideproject.cafe_cok.admin.dto.request;

import com.sideproject.cafe_cok.admin.domain.CafeCopy;
import com.sideproject.cafe_cok.utils.FormatConverter;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AdminCafeSaveRequest {

    @NotNull
    private String name;
    @NotNull
    private String roadAddress;
    @NotNull
    private Integer mapx;
    @NotNull
    private Integer mapy;
    private String telephone;

    protected AdminCafeSaveRequest() {
    }

    public AdminCafeSaveRequest(final String name, final String roadAddress,
                                final Integer mapx, final Integer mapy, final String telephone) {
        this.name = name;
        this.roadAddress = roadAddress;
        this.mapx = mapx;
        this.mapy = mapy;
        this.telephone = telephone;
    }

    public CafeCopy toEntity(final String mainImage) {
        return new CafeCopy(
                name,
                FormatConverter.convertFormatPhoneNumber(telephone),
                roadAddress,
                mapx,
                mapy,
                mainImage);
    }
}
