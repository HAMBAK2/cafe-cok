package com.sideproject.cafe_cok.admin.dto.request;

import com.sideproject.cafe_cok.cafe.domain.Cafe;
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

    public Menu toEntity(final Cafe cafe) {
        return new Menu(this.name, this.price, cafe);
    }

}
