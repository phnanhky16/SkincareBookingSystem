package com.skincare_booking_system.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ImagesService {
    public String uploadImage(MultipartFile file) throws IOException;
}
