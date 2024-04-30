package com.sideproject.hororok.cafe.domain.repository;

import com.sideproject.hororok.cafe.domain.CafeImage;
import com.sideproject.hororok.cafe.exception.NoSuchCafeImageException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CafeImageRepository extends JpaRepository<CafeImage, Long> {


    @Query("SELECT ci.imageUrl FROM CafeImage ci " +
            "WHERE ci.cafe.id = :cafeId")
    List<String> findImageUrlsByCafeId(final Long cafeId, final Pageable pageable);


    List<CafeImage> findByCafeId(final Long cafeId);

    @Query("SELECT ci.imageUrl " +
            "FROM CafeImage ci " +
            "WHERE ci.cafe.id = :cafeId " +
            "ORDER BY ci.createdDate ASC " +
            "LIMIT 1")
    Optional<String> findOneImageUrlByCafeId(final Long cafeId);

    default String getOneImageUrlByCafeId(final Long cafeId) {
        return findOneImageUrlByCafeId(cafeId)
                .orElseThrow(NoSuchCafeImageException::new);
    }

}
