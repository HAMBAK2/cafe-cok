package com.sideproject.cafe_cok.utils.S3.component;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.sideproject.cafe_cok.utils.S3.exception.FileUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
    private final int maxRetries = 10;

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
        String key = extractObjectKeyFromUrl(url);
        amazonS3Client.deleteObject(bucket, key);
    }

    public boolean isExistObject(String objectName) {
        int attempt = 0;
        boolean exists = false;

        while(attempt < maxRetries) {
            exists = amazonS3Client.doesObjectExist(bucket, objectName);

            if(exists) return true;

            attempt++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new FileUploadException();
            }
        }

        return false;
    }

    public boolean isExistObject(List<String> objectNames) {
        boolean exists = true;
        for (String objectName : objectNames) {
            exists = isExistObject(objectName);
            if(!exists) return false;
        }

        return true;
    }

    public String upload(File uploadFile, String dirName) {
        int lastIndex = uploadFile.getName().lastIndexOf(".");
        String extension = uploadFile.getName().substring(lastIndex + 1);
        File compressFile = compressAndRotate(uploadFile, extension);
        String fileName = dirName + "/" + UUID.randomUUID() + "." + extension;
        String uploadImageUrl = putS3(compressFile, fileName);

        removeNewFile(uploadFile);
        removeNewFile(compressFile);
        return uploadImageUrl.replace(s3UrlPrefix, cloudFrontDomain);
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private File compressAndRotate(File file, String extension) {
        try {
            BufferedImage image = rotate(file);
            File compressed = new File(file.getParent(), file.getName() + "_compressed." + extension);

            ImageWriter writer = ImageIO.getImageWritersByFormatName(extension).next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(compressed);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            if(param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.5f);
            }

            writer.write(null, new IIOImage(image, null, null), param);

            ios.close();
            writer.dispose();
            return compressed;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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

        return Optional.empty();
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

    private BufferedImage rotate(File file) throws IOException {

        int orientation = 1;
        Metadata metadata;
        Directory directory;

        try {
            metadata = ImageMetadataReader.readMetadata(file);
            directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if(directory != null) {
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        BufferedImage bfImage = ImageIO.read(file);

        switch (orientation) {
            case 1:
                break;
            case 3:
                bfImage = Scalr.rotate(bfImage, Scalr.Rotation.CW_180, null);
            case 6:
                bfImage = Scalr.rotate(bfImage, Scalr.Rotation.CW_90, null);
                break;
            case 8:
                bfImage = Scalr.rotate(bfImage, Scalr.Rotation.CW_270, null);
                break;
            default:
                orientation = 1;
                break;
        }

        return bfImage;
    }


}
