package com.sideproject.cafe_cok.util.tmap.dto.response;

import com.sideproject.cafe_cok.util.tmap.dto.TmapFeature;
import lombok.Getter;

import java.util.List;

@Getter
public class TmapApiResponse {

    private List<TmapFeature> features;

}