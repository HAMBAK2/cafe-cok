package com.sideproject.cafe_cok.admin.application;


import com.sideproject.cafe_cok.admin.domain.CafeCopy;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveRequest;
import com.sideproject.cafe_cok.admin.domain.repository.CafeCopyRepository;
import com.sideproject.cafe_cok.admin.dto.request.AdminCafeSaveTestRequest;
import com.sideproject.cafe_cok.admin.dto.response.AdminCafeSaveResponse;
import com.sideproject.cafe_cok.menu.domain.repository.MenuRepository;
import com.sideproject.cafe_cok.utils.Constants;
import com.sideproject.cafe_cok.utils.FormatConverter;
import com.sideproject.cafe_cok.utils.S3.component.S3Uploader;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final CafeCopyRepository cafeCopyRepository;
    private final MenuRepository menuRepository;
    private final S3Uploader s3Uploader;

    private final String CAFE_ORIGIN_IMAGE_DIR = "cafe";

    @Transactional
    public AdminCafeSaveResponse saveCafe(final AdminCafeSaveRequest request,
                                          final MultipartFile mainImage, final List<MultipartFile> otherImages) {


        //전화번호 없으면 빈 문자열
        //메뉴 없으면 빈 리스트
        //base64 -> file 변환시 생성되는 파일도 삭제해 줘야 함.

        //카페 이미지 저장
        String mainImageUrl = s3Uploader.upload(mainImage, CAFE_ORIGIN_IMAGE_DIR).replace(Constants.IMAGE_URL_PREFIX, "");
        CafeCopy cafeCopy = request.toEntity(mainImageUrl);

        //카페 저장
        CafeCopy savedCafeCopy = cafeCopyRepository.save(cafeCopy);

        return AdminCafeSaveResponse.of(savedCafeCopy);
    }

    @Transactional
    public AdminCafeSaveResponse saveCafeTest(final AdminCafeSaveTestRequest request, final MultipartFile mainImage, final List<MultipartFile> otherImages) {


        String mainImageUrl = "메인 이미지 URL";
        CafeCopy cafeCopy = request.toEntity(mainImageUrl);
        File convertedFile = FormatConverter.convertBase64StringToFile(request.getMenus().get(0).getImage());
        CafeCopy savedCafeCopy = cafeCopyRepository.save(cafeCopy);
        return AdminCafeSaveResponse.of(savedCafeCopy);
    }




}
