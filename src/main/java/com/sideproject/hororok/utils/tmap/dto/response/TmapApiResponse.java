package com.sideproject.hororok.utils.tmap.dto.response;

import com.sideproject.hororok.utils.tmap.dto.TmapFeature;
import lombok.Getter;

import java.util.List;

@Getter
public class TmapApiResponse {

    private List<TmapFeature> features;

}