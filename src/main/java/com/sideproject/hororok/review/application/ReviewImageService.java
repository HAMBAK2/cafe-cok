package com.sideproject.hororok.review.application;

import com.sideproject.hororok.aop.annotation.LogTrace;
import com.sideproject.hororok.review.domain.ReviewImageRepository;
import com.sideproject.hororok.review.dto.ReviewImageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewImageService {

    private final ReviewImageRepository reviewImageRepository;


}
