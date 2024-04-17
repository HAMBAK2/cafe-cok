package com.sideproject.hororok.cafe.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CafeImageRepository extends JpaRepository<CafeImage, Long> {

    @Query(
            "SELECT ci.imageUrl FROM CafeImage ci " +
            "WHERE ci.cafe.id = :cafeId")
    List<String> findCafeImagesUrlsByCafeId(Long cafeId);


    List<CafeImage> findByCafeId(Long cafeId);

}
