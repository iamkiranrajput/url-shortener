package com.guardians.service;

import com.guardians.dto.UrlMappingDto;
import com.guardians.dto.UrlRequestDto;
import com.guardians.model.UrlMapping;

import java.util.List;
import java.util.Optional;

public interface UrlShortenerService {
    UrlMapping createShortUrl(UrlRequestDto urlRequest);
    Optional<UrlMappingDto> getOriginalUrl(String shortUrl);
    List<UrlMappingDto> getAllRecord();
}
