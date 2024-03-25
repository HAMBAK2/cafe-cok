package com.sideproject.hororok.image.repository;

import com.sideproject.hororok.image.dto.ImageDto;
import com.sideproject.hororok.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findImageByCafeId(Long cafeId);
}
