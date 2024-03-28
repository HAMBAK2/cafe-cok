package com.sideproject.hororok.cafe.cond;


import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class CafeCategorySearchCond {

    private BigDecimal longitude;
    private BigDecimal latitude;
    private List<String> keywords;
}
