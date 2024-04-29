package com.sideproject.hororok.cafe.domain.repository;

import com.sideproject.hororok.cafe.domain.CafeImage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CafeImageRepository extends JpaRepository<CafeImage, Long> {

    @Query("SELECT ci.imageUrl FROM CafeImage ci " +
            "WHERE ci.cafe.id = :cafeId")
    List<String> findCafeImagesUrlsByCafeId(Long cafeId);


    @Query("SELECT ci.imageUrl FROM CafeImage ci " +
            "WHERE ci.cafe.id = :cafeId")
    List<String> findImageUrlsByCafeId(Long cafeId, Pageable pageable);


    List<CafeImage> findByCafeId(Long cafeId);

}
