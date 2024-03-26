package com.sideproject.hororok.cafe.cond;


import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CafeCategorySearchCond {

    private BigDecimal longitude;
    private BigDecimal latitude;
    private String purpose;
    private String menu;
    private String theme;
    private String facility;
    private String mood;

}
