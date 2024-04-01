package com.sideproject.hororok.cafeImage.repository;

import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.cafeImage.entity.CafeImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CafeImageRepository extends JpaRepository<CafeImage, Long> {

    @LogTrace
    @Query(
            "SELECT ci.imageUrl FROM CafeImage ci " +
            "WHERE ci.cafe.id = :cafeId")
    List<String> findCafeImagesUrlsByCafeId(Long cafeId);


    @LogTrace
    List<CafeImage> findByCafeId(Long cafeId);

}
