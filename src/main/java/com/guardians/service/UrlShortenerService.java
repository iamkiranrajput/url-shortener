package com.guardians.service;

import com.guardians.dto.UrlMappingDto;
import com.guardians.dto.UrlRequest;
import com.guardians.dto.UrlResponse;
import com.guardians.model.UrlMapping;

import java.util.List;
import java.util.Optional;

public interface UrlShortenerService {
    UrlResponse createShortUrl(UrlRequest urlRequest);
    UrlResponse getOriginalUrl(String shortUrl);
    UrlResponse getAllRecord();
}
