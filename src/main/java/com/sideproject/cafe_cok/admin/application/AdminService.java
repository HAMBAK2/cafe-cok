package com.sideproject.cafe_cok.admin.application;


import com.sideproject.cafe_cok.admin.domain.CafeCopy;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveRequest;
import com.sideproject.cafe_cok.admin.domain.repository.CafeCopyRepository;
import com.sideproject.cafe_cok.admin.dto.response.AdminCafeSaveResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final CafeCopyRepository cafeCopyRepository;


    @Transactional
    public AdminCafeSaveResponse saveCafe(final AdminCafeSaveRequest request,
                                          final MultipartFile mainImage, final List<MultipartFile> otherImages) {

        String mainImageUrl = "메인 이미지 URL";
        CafeCopy cafeCopy = request.toEntity(mainImageUrl);
        CafeCopy savedCafeCopy = cafeCopyRepository.save(cafeCopy);

        return AdminCafeSaveResponse.of(savedCafeCopy);
    }



}
