package com.guardians.service;


import com.guardians.dto.UrlRequestDto;
import com.guardians.dto.UrlMappingDto;
import com.guardians.model.UrlMapping;
import com.guardians.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlShortenerServiceImpl {
    private final UrlMappingRepository urlMappingRepository;

    public UrlShortenerServiceImpl(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    public UrlMapping createShortUrl(UrlRequestDto urlRequest) {
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(urlRequest.getOriginalUrl());
        urlMapping.setShortUrl(UUID.randomUUID().toString().substring(0, 6)); // Generate a random hash
        urlMapping.setCreatedAt(LocalDateTime.now());
        urlMapping.setExpiresAt(urlRequest.getExpiresAt());
        urlMapping.setUsageLimit(urlRequest.getUsageLimit());
        urlMapping.setUsageCount(0);
        return urlMappingRepository.save(urlMapping);
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
