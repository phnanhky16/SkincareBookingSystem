package com.skincare_booking_system.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.skincare_booking_system.service.ImagesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImagesServiceImpl implements ImagesService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String publicValue = generatePublicValue(file.getOriginalFilename());
        String extension = getFileExtension(file.getOriginalFilename());

        // Convert MultipartFile to File
        File fileUpload = convert(file);
        log.info("Uploading file: {}", fileUpload.getAbsolutePath());

        // Upload to Cloudinary
        cloudinary.uploader().upload(fileUpload, ObjectUtils.asMap("public_id", publicValue));

        // Delete temp file after upload
        cleanDisk(fileUpload);

        return cloudinary.url().secure(true).generate(publicValue + "." + extension);
    }

    private String generatePublicValue(String originalName) {
        String fileName = getFileNameWithoutExtension(originalName);
        return UUID.randomUUID().toString() + "_" + fileName;
    }

    private File convert(MultipartFile file) throws IOException {
        File convFile = File.createTempFile("upload_", "." + getFileExtension(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    private void cleanDisk(File file) {
        try {
            Files.deleteIfExists(file.toPath());
            log.info("Deleted temp file: {}", file.getAbsolutePath());
        } catch (IOException e) {
            log.error("Error deleting temp file", e);
        }
    }

    private String getFileNameWithoutExtension(String originalName) {
        int lastDot = originalName.lastIndexOf(".");
        return (lastDot == -1) ? originalName : originalName.substring(0, lastDot);
    }

    private String getFileExtension(String originalName) {
        int lastDot = originalName.lastIndexOf(".");
        return (lastDot == -1) ? "" : originalName.substring(lastDot + 1);
    }
}
