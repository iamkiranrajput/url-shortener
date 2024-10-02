package com.guardians.service;

import com.guardians.auth.entities.User;
import com.guardians.dto.UrlMappingDto;
import com.guardians.dto.UrlPageResponse;
import com.guardians.dto.UrlRequest;
import com.guardians.dto.UrlResponse;
import com.guardians.model.UrlMapping;

import java.util.List;
import java.util.Optional;

public interface UrlShortenerService {
    UrlResponse createShortUrl(UrlRequest urlRequest);
    UrlResponse getOriginalUrl(String shortUrl);
    UrlResponse getAllRecords();
    UrlPageResponse getAllUrlWithPagination(Integer pageNumber, Integer pageSize);
    UrlPageResponse getAllUrlWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir);
    UrlResponse getUrlsByUser(User user);
}
