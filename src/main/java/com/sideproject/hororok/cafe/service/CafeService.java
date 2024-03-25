package com.sideproject.hororok.cafe.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.sideproject.hororok.Menu.dto.MenuDto;
import com.sideproject.hororok.Menu.repository.MenuRepository;
import com.sideproject.hororok.Menu.service.MenuService;
import com.sideproject.hororok.cafe.dto.CafeDetailDto;
import com.sideproject.hororok.cafe.entity.Cafe;
import com.sideproject.hororok.cafe.repository.CafeRepository;
import com.sideproject.hororok.image.dto.ImageDto;
import com.sideproject.hororok.image.service.ImageService;
import com.sideproject.hororok.keword.dto.KeywordDto;
import com.sideproject.hororok.review.dto.ReviewDto;
import com.sideproject.hororok.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {

    private final ImageService imageService;
    private final ReviewService reviewService;
    private final CafeRepository cafeRepository;
    private final MenuService menuService;

    @Transactional
    public CafeDetailDto findCafeDetail(Long cafeId){

        Cafe cafe =  findCafeById(cafeId);
        List<MenuDto> menus = menuService.findByCafeId(cafeId);
        List<ImageDto> images = imageService.findImageByCafeId(cafeId);
        List<ReviewDto> reviews = reviewService.findReviewByCafeId(cafeId);

        //리뷰중에서 태그의 개수가 많은 거 3개 뽑아야함
        List<KeywordDto> cafeKeywords = reviewService.findKeywordInReviewByCafeIdWithKeywordCount(cafe.getId(), 3);
        List<String> cafeImageUrls = makeCafeImageUrls(cafe, images);

        return CafeDetailDto.of(cafe, menus, images, reviews, cafeKeywords, cafeImageUrls);
    }


    private Cafe findCafeById(Long cafeId) {
        return cafeRepository.findById(cafeId)
                .orElseThrow(() -> new NotFoundException("cafe not found : " + cafeId));
    }

    private List<String> makeCafeImageUrls(Cafe cafe, List<ImageDto> images) {

        List<String> cafeImageUrls = new ArrayList<>();
        cafeImageUrls.add(cafe.getImageUrl1());
        cafeImageUrls.add(cafe.getImageUrl2());
        cafeImageUrls.add(cafe.getImageUrl3());

        for (int i = 0; i < 2; i++) cafeImageUrls.add(images.get(i).getImageUrl());

        return cafeImageUrls;
    }




}
