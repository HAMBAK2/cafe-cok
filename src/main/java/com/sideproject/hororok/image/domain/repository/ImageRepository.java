package com.sideproject.hororok.image.domain.repository;

import com.sideproject.hororok.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
