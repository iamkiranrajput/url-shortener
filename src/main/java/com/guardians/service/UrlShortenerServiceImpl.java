package com.guardians.service;


import com.guardians.dto.UrlRequest;
import com.guardians.dto.UrlMappingDto;
import com.guardians.dto.UrlResponse;
import com.guardians.model.UrlMapping;
import com.guardians.repository.UrlMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UrlShortenerServiceImpl {
    UrlResponse urlResponse = new UrlResponse();
    private final UrlMappingRepository urlMappingRepository;
    public UrlShortenerServiceImpl(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }



    public UrlResponse createShortUrl(UrlRequest urlRequest) {
        try {
            UrlMapping urlMapping = new UrlMapping();

            urlMapping.setOriginalUrl(urlRequest.getOriginalUrl());
            urlMapping.setShortUrl(UUID.randomUUID().toString().substring(0, 6)); // Generate a random hash
            urlMapping.setCreatedAt(LocalDateTime.now());
            urlMapping.setExpiresAt(urlRequest.getExpiresAt());
            urlMapping.setUsageLimit(urlRequest.getUsageLimit());
            urlMapping.setUsageCount(0);

            UrlMapping savedUrl = urlMappingRepository.save(urlMapping);

            log.info("Shortener URL Generated Successfully Shortener Url is :: {}", savedUrl.getShortUrl());
            urlResponse.setData(savedUrl);
            urlResponse.setMessage("Employee Registered Successfully");
            urlResponse.setStatus(201);
        } catch (Exception ex) {
            log.error("An error occurred during Url Shortening :: {}", ex.getMessage());
            throw new RuntimeException("An error occurred during Url Shortening. Please try again.");

        }
        return urlResponse;
    }


    public Optional<UrlMappingDto> getOriginalUrl(String shortUrl) {
        return urlMappingRepository.findByShortUrl(shortUrl);
    }

    public boolean isUrlExpiredOrLimitReached(UrlMappingDto urlResponseDTO) {
        return urlResponseDTO.getExpiresAt().isBefore(LocalDateTime.now()) || urlResponseDTO.getUsageCount() >= urlResponseDTO.getUsageLimit();
    }
    public void incrementUsageCount(UrlMappingDto urlResponseDTO) {
        UrlMapping urlMapping= new UrlMapping();
        urlMapping.setUsageCount(urlMapping.getUsageCount() + 1);
        urlMappingRepository.save(urlMapping);
    }

    public List<UrlMappingDto> getAllRecord(){
        List data = urlMappingRepository.findAll();
        return data;
    }
}
