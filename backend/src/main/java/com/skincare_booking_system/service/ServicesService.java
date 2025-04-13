package com.skincare_booking_system.service;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.skincare_booking_system.dto.response.ServicesResponse;
import com.skincare_booking_system.entities.Package;
import com.skincare_booking_system.entities.Services;
import com.skincare_booking_system.exception.AppException;
import com.skincare_booking_system.exception.ErrorCode;
import com.skincare_booking_system.mapper.ServicesMapper;
import com.skincare_booking_system.repository.PackageRepository;
import com.skincare_booking_system.repository.ServicesRepository;

@Service
public class ServicesService {
    @Autowired
    private ServicesRepository servicesRepository;

    @Autowired
    private ImagesService imagesService;

    @Autowired
    private ServicesMapper servicesMapper;

    @Autowired
    private PackageRepository packageRepository;

    public ServicesResponse createServices(
            String serviceName,
            String description,
            String category,
            Double price,
            LocalTime duration,
            Boolean isActive,
            MultipartFile imgUrl)
            throws IOException {
        // Upload image only if provided
        String imageUrl = (imgUrl != null && !imgUrl.isEmpty()) ? imagesService.uploadImage(imgUrl) : null;

        Services service = Services.builder()
                .serviceName(serviceName)
                .description(description)
                .category(category)
                .price(price)
                .duration(duration)
                .imgUrl(imageUrl)
                .isActive(isActive)
                .build();

        servicesRepository.save(service);
        return servicesMapper.toServicesResponse(service);
    }

    public ServicesResponse searchServiceId(long serviceId) {
        Optional<Services> service = servicesRepository.findByServiceId(serviceId);
        ServicesResponse response = new ServicesResponse();
        response.setServiceName(service.get().getServiceName());
        response.setDescription(service.get().getDescription());
        response.setCategory(service.get().getCategory());
        response.setServiceId(service.get().getServiceId());
        response.setDuration(service.get().getDuration());
        response.setImgUrl(service.get().getImgUrl());
        response.setPrice(service.get().getPrice());
        response.setIsActive(service.get().getIsActive());
        return response;
    }

    public List<ServicesResponse> getAllServices() {
        List<Services> services = servicesRepository.findAll();
        if (services.isEmpty()) {
            throw new AppException(ErrorCode.SERVICE_NOT_FOUND);
        }
        return services.stream().map(servicesMapper::toServicesResponse).collect(Collectors.toList());
    }

    public List<ServicesResponse> getAllServicesIsActiveTrue() {
        List<Services> activeServices = servicesRepository.findByIsActiveTrue();
        if (activeServices.isEmpty()) {
            throw new AppException(ErrorCode.SERVICE_NOT_FOUND);
        }
        return activeServices.stream().map(servicesMapper::toServicesResponse).toList();
    }

    public List<ServicesResponse> getAllServicesIsActiveFalse() {

        List<Services> activeServices = servicesRepository.findByIsActiveFalse();
        if (activeServices.isEmpty()) {
            throw new AppException(ErrorCode.SERVICE_NOT_FOUND);
        }
        return activeServices.stream().map(servicesMapper::toServicesResponse).toList();
    }

    public List<ServicesResponse> getServicesByServicesNameCUS(String serviceName) {
        List<Services> services =
                servicesRepository.findServicessByServiceNameContainingIgnoreCaseAndIsActiveTrue(serviceName);
        if (services.isEmpty()) {
            throw new AppException(ErrorCode.SERVICE_NOT_FOUND);
        }
        return services.stream().map(servicesMapper::toServicesResponse).collect(Collectors.toList());
    }

    public List<ServicesResponse> getServicesByServicesName(String serviceName) {
        List<Services> services = servicesRepository.findServicessByServiceNameContainingIgnoreCase(serviceName);
        if (services.isEmpty()) {
            throw new AppException(ErrorCode.SERVICE_NOT_FOUND);
        }
        return services.stream().map(servicesMapper::toServicesResponse).collect(Collectors.toList());
    }

    public ServicesResponse updateServices(
            long serviceId,
            String serviceName,
            String description,
            String category,
            Double price,
            LocalTime duration,
            MultipartFile imgUrl)
            throws IOException {
        Services service = servicesRepository
                .findByServiceId(serviceId)
                .orElseThrow(() -> new RuntimeException("Service with ID '" + serviceId + "' not found"));
        String imageUrl = service.getImgUrl(); // Giữ ảnh cũ nếu không upload mới
        if (imgUrl != null && !imgUrl.isEmpty()) {
            imageUrl = imagesService.uploadImage(imgUrl);
        }
        service.setServiceName(serviceName);
        service.setPrice(price);
        service.setDescription(description);
        service.setCategory(category);
        service.setDuration(duration);
        service.setImgUrl(imageUrl);
        servicesRepository.save(service);
        return servicesMapper.toServicesResponse(service);
    }

    public String deactivateServices(long serviceId) {
        Services services = servicesRepository
                .findByServiceId(serviceId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));
        services.setIsActive(false);
        servicesRepository.save(services);
        List<Package> packages = packageRepository.findByServicesIn(List.of(services));
        for (Package p : packages) {
            p.setPackageActive(false);
            packageRepository.save(p);
        }
        return "Services deactivated successfully";
    }

    public String activateServices(long serviceId) {
        Services services = servicesRepository
                .findByServiceId(serviceId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));
        services.setIsActive(true);
        servicesRepository.save(services);
        return "Service activated successfully";
    }

    public Long countAllServices() {
        return servicesRepository.countAllServices();
    }
}
