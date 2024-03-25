package com.sideproject.hororok.image.service;


import com.sideproject.hororok.image.dto.ImageDto;
import com.sideproject.hororok.image.entity.Image;
import com.sideproject.hororok.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public List<ImageDto> findImageByCafeId(Long cafeId) {

        List<Image> images = imageRepository.findImageByCafeId(cafeId);
        List<ImageDto> imageDtos = new ArrayList<>();
        for (Image image : images) {
            imageDtos.add(ImageDto.from(image));
        }

        return imageDtos;
    }



}
