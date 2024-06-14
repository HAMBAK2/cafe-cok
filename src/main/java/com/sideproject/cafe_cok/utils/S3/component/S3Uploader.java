package com.sideproject.cafe_cok.utils.S3.component;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.sideproject.cafe_cok.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sideproject.cafe_cok.utils.Constants.DECODED_URL_SPLIT_STR;
import static com.sideproject.cafe_cok.utils.Constants.URL_DECODER_DECODE_ENC;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.url-prefix}")
    private String s3UrlPrefix;

    @Value("${cloud.aws.cloud-front.domain}")
    private String cloudFrontDomain;

    public String upload(MultipartFile multipartFile, String dirName) {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        return upload(uploadFile, dirName);
    }

    public void delete(String url) {
        System.out.println("딜리트 위한 URL : " + url);
        String key = extractObjectKeyFromUrl(url);
        System.out.println("딜리트 위한 키 : " + key);
        amazonS3Client.deleteObject(bucket, key);
    }

    public String upload(File uploadFile, String dirName) {
        int lastIndex = uploadFile.getName().lastIndexOf(".");
        String extension = uploadFile.getName().substring(lastIndex + 1);
        String fileName = dirName + "/" + UUID.randomUUID() + "." + extension;
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl.replace(s3UrlPrefix, cloudFrontDomain);
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) {
        try {
            File convertFile = new File(file.getOriginalFilename());
            boolean created = convertFile.createNewFile();
            if (created) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(file.getBytes());
                }
                return Optional.of(convertFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty(); // 파일 생성 실패 시 빈 Optional 반환
    }

    private String extractObjectKeyFromUrl(String url) {
        try {
            String decodedUrl = URLDecoder.decode(url, URL_DECODER_DECODE_ENC);
            String[] parts = decodedUrl.split(DECODED_URL_SPLIT_STR);
            if (parts.length == 2) {
                return parts[1];
            } else {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
