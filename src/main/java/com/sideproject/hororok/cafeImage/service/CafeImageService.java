package com.sideproject.hororok.cafeImage.service;

import com.sideproject.hororok.cafeImage.repository.CafeImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeImageService {

    private final CafeImageRepository cafeImageRepository;

    public List<String> findCafeImageUrlsByCafeId(Long cafeId) {
        return cafeImageRepository.findCafeImagesUrlsByCafeId(cafeId);
    }
}
