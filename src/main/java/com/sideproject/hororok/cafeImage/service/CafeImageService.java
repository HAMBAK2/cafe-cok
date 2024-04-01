package com.sideproject.hororok.cafeImage.service;

import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.cafeImage.entity.CafeImage;
import com.sideproject.hororok.cafeImage.repository.CafeImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CafeImageService {

    private final CafeImageRepository cafeImageRepository;

    public List<String> findCafeImageUrlsByCafeId(Long cafeId) {
        return cafeImageRepository.findCafeImagesUrlsByCafeId(cafeId);
    }

    public Optional<String> findOneImageUrlByCafeId(Long cafeId) {
        List<CafeImage> cafeImages = cafeImageRepository.findByCafeId(cafeId);
        if (!cafeImages.isEmpty()) {
            return Optional.of(cafeImages.get(0).getImageUrl());
        }
        return Optional.empty();
    }
}
