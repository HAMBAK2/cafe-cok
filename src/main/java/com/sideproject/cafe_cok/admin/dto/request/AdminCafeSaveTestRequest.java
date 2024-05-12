package com.sideproject.cafe_cok.admin.dto.request;

import com.amazonaws.services.ec2.model.transform.LicenseConfigurationRequestStaxUnmarshaller;
import com.sideproject.cafe_cok.admin.domain.CafeCopy;
import com.sideproject.cafe_cok.menu.dto.MenuDto;
import com.sideproject.cafe_cok.utils.FormatConverter;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class AdminCafeSaveTestRequest {

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


    protected AdminCafeSaveTestRequest() {
    }

    public AdminCafeSaveTestRequest(final String name, final String roadAddress,
                                    final Integer mapx, final Integer mapy,
                                    final String telephone, final List<AdminMenuSaveRequest> menus) {
        this.name = name;
        this.roadAddress = roadAddress;
        this.mapx = mapx;
        this.mapy = mapy;
        this.telephone = telephone;
        this.menus = menus;
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
