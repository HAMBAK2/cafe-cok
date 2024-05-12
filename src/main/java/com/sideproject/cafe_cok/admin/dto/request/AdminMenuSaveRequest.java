package com.sideproject.cafe_cok.admin.dto.request;

import com.sideproject.cafe_cok.admin.domain.CafeCopy;
import com.sideproject.cafe_cok.admin.domain.MenuCopy;
import com.sideproject.cafe_cok.menu.domain.Menu;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Optional;

@Getter
public class AdminMenuSaveRequest {

    @NotNull
    private String name;
    @NotNull
    private Integer price;
    private String image;

    protected AdminMenuSaveRequest() {}

    public AdminMenuSaveRequest(final String name, final Integer price, final String image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public MenuCopy toEntity(final String imageUrl, final CafeCopy cafeCopy) {
        return new MenuCopy(this.name, this.price, imageUrl, cafeCopy);
    }

}
