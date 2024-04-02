package com.sideproject.hororok.category.dto;

import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CategoryKeywords {

    private List<String> purpose;
    private List<String> atmosphere;
    private List<String> facility;
    private List<String> menu;
    private List<String> theme;

}
