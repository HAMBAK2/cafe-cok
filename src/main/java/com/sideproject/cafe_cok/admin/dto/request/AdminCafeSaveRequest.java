package com.sideproject.cafe_cok.admin.dto.request;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
import com.sideproject.cafe_cok.utils.FormatConverter;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
    private List<AdminMenuSaveRequest> menus;

    protected AdminCafeSaveRequest() {
    }

    public AdminCafeSaveRequest(final String name, final String roadAddress,
                                    final Integer mapx, final Integer mapy,
                                    final String telephone, final List<AdminMenuSaveRequest> menus) {
        this.name = name;
        this.roadAddress = roadAddress;
        this.mapx = mapx;
        this.mapy = mapy;
        this.telephone = telephone;
        this.menus = menus;
    }

    public AdminCafeSaveRequest(final String name, final String roadAddress,
                                final Integer mapx, final Integer mapy, final String telephone) {
        this.name = name;
        this.roadAddress = roadAddress;
        this.mapx = mapx;
        this.mapy = mapy;
        this.telephone = telephone;
        this.menus = new ArrayList<>();
    }

    public Cafe toEntity(final String mainImage) {
        return new Cafe(
                name,
                FormatConverter.convertFormatPhoneNumber(telephone),
                roadAddress,
                mapx,
                mapy,
                mainImage);
    }
}
