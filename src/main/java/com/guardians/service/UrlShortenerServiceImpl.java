package com.guardians.service;

import com.guardians.auth.entities.User;
import com.guardians.dto.UrlMappingDto;
import com.guardians.dto.UrlPageResponse;
import com.guardians.dto.UrlRequest;
import com.guardians.dto.UrlResponse;
import com.guardians.exception.PaginationException;
import com.guardians.exception.ShortUrlNotFoundException;
import com.guardians.model.UrlMapping;
import com.guardians.repository.UrlMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UrlShortenerServiceImpl implements UrlShortenerService {

    private final UrlMappingRepository urlMappingRepository;

    public UrlShortenerServiceImpl(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    public UrlResponse createShortUrl(UrlRequest urlRequest) {
        UrlResponse urlResponse = new UrlResponse();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            UrlMapping urlMapping = new UrlMapping();
            urlMapping.setOriginalUrl(urlRequest.getOriginalUrl());
            urlMapping.setShortUrl(UUID.randomUUID().toString().substring(0, 6)); // Generate a random hash
            urlMapping.setCreatedAt(LocalDateTime.now());
            urlMapping.setExpiresAt(urlRequest.getExpiresAt());
            urlMapping.setUsageLimit(urlRequest.getUsageLimit());
            urlMapping.setUsageCount(0);
            urlMapping.setUser(user);

            UrlMapping savedUrl = urlMappingRepository.save(urlMapping);

            UrlMappingDto dto =new UrlMappingDto(
                    savedUrl.getId(),
                    savedUrl.getOriginalUrl(),
                    savedUrl.getShortUrl(),
                    savedUrl.getCreatedAt(),
                    savedUrl.getExpiresAt(),
                    savedUrl.getUsageLimit(),
                    savedUrl.getUsageCount(),
                    savedUrl.getUser().getUserId()
            );

            log.info("Shortened URL generated successfully. Short URL: {}", savedUrl.getShortUrl());
            urlResponse.setData(dto);
            urlResponse.setMessage("Shortened URL generated successfully.");
            urlResponse.setStatus(HttpStatus.CREATED.value());
        } catch (DataIntegrityViolationException ex) {
            log.error("Data integrity violation during URL shortening: {}", ex.getMessage());
            throw new RuntimeException("Short URL already exists. Please try again.");
        } catch (Exception ex) {
            log.error("An error occurred during URL shortening: {}", ex.getMessage());
            throw new RuntimeException("An error occurred while shortening the URL. Please try again.");
        }
        return urlResponse;
    }

    public UrlResponse getOriginalUrl(String shortUrl) {
        UrlResponse urlResponse = new UrlResponse();
        try {
            log.info("Fetching original URL for short URL: {}", shortUrl);
            UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl)
                    .orElseThrow(() -> new ShortUrlNotFoundException("Short URL not found: " + shortUrl));

            if (isUrlExpiredOrLimitReached(urlMapping)) {
                urlResponse.setMessage("URL has expired or reached its usage limit.");
                urlResponse.setStatus(HttpStatus.GONE.value());
                log.warn("Short URL {} has expired or reached its usage limit.", shortUrl);
                return urlResponse;
            } else {
                urlResponse.setData(urlMapping);
                urlResponse.setMessage("Original URL fetched successfully.");
                urlResponse.setStatus(HttpStatus.ACCEPTED.value());
                log.info("Original URL fetched successfully for short URL: {}", shortUrl);

                incrementUsageCount(urlMapping); // Increment usage count
            }
        } catch (ShortUrlNotFoundException ex) {
            log.error("Short URL {} not found: {}", shortUrl, ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("An error occurred while fetching the original URL: {}", ex.getMessage());
            throw new RuntimeException("Error occurred while fetching the original URL. Please try again.");
        }
        return urlResponse;
    }


    private boolean isUrlExpiredOrLimitReached(UrlMapping urlMapping) {
        return urlMapping.getExpiresAt().isBefore(LocalDateTime.now()) ||
                urlMapping.getUsageCount() >= urlMapping.getUsageLimit();
    }
    private void incrementUsageCount(UrlMapping urlMapping) {
        urlMapping.setUsageCount(urlMapping.getUsageCount() + 1);
        urlMappingRepository.save(urlMapping); // Saving the updated entity
    }



    @Override
    public UrlResponse getAllRecords() {
        UrlResponse urlResponse = new UrlResponse();
        try {
            log.info("Fetching all URL records");
            List<UrlMapping> urlRecords = urlMappingRepository.findAll();
            List<UrlMappingDto> urlMappingDtos = new ArrayList<>();
            for (UrlMapping urlMapping : urlRecords) {
                urlMappingDtos.add(new UrlMappingDto(
                        urlMapping.getId(),
                        urlMapping.getOriginalUrl(),
                        urlMapping.getShortUrl(),
                        urlMapping.getCreatedAt(),
                        urlMapping.getExpiresAt(),
                        urlMapping.getUsageLimit(),
                        urlMapping.getUsageCount(),
                        urlMapping.getUser().getUserId() // Make sure User is fetched or mapped properly
                ));
            }
            if (urlMappingDtos.isEmpty()) {
                log.warn("No URL records found.");
                urlResponse.setMessage("No URL records found.");
                urlResponse.setStatus(HttpStatus.NOT_FOUND.value());
            } else {
                urlResponse.setData(urlMappingDtos);
                urlResponse.setMessage("URL records fetched successfully.");
                urlResponse.setStatus(HttpStatus.ACCEPTED.value());
            }
        } catch (Exception ex) {
            log.error("An error occurred while fetching URL records: {}", ex.getMessage());
            throw new RuntimeException("Error occurred while fetching URL records. Please try again.");
        }
        return urlResponse;
    }


    @Override
    public UrlPageResponse getAllUrlWithPagination(Integer pageNumber, Integer pageSize) {
            try{
            log.info("Fetching Urls with pagination: pageNumber={}, pageSize={} ", pageNumber, pageSize);
            PageRequest pageable = PageRequest.of(pageNumber, pageSize);
            Page<UrlMapping> urlPages = urlMappingRepository.findAll(pageable);
            List<UrlMapping> urls = urlPages.getContent();

            List<UrlMappingDto> urlMappingDto = getMappingDtos(urls);

            log.info("Pagination Result: totalELement={}, totalPages={}, isLast={} ", urlPages.getTotalElements(), urlPages.getTotalPages(), urlPages.isLast());
            return new UrlPageResponse(urlMappingDto, pageNumber, pageSize, urlPages.getTotalElements(), urlPages.getTotalPages(), urlPages.isLast());
        } catch(IllegalArgumentException ex) {
            log.error("Invalid arguments provided for pagination: pageNumber={}, pageSize={}", pageNumber, pageSize, ex);
            throw new PaginationException("Invalid page number or page size provided", ex);
        } catch(Exception ex){
            log.error("An error occurred during pagination: {}", ex.getMessage(), ex);
            throw new PaginationException("An error occurred while fetching paginated URLs", ex);
        }
    }




    @Override
    public UrlPageResponse getAllUrlWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        try{
        log.info("Fetching URLs with pagination and sorting: pageNumber={}, pageSize={}, sortBy={}, direction={}",pageNumber, pageSize, sortBy, dir);

        Sort sort =dir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending() :Sort.by(sortBy).descending();

        PageRequest pageable =PageRequest.of(pageNumber,pageSize,sort);
        Page<UrlMapping> urlPages = urlMappingRepository.findAll(pageable);
        List<UrlMapping>urls =urlPages.getContent();

        log.info("Sorting applied: {} {}", sortBy, dir);
        log.info("Pagination result: totalElements={}, totalPages={}, isLastPage={}",urlPages.getTotalElements(), urlPages.getTotalPages(), urlPages.isLast());
            List<UrlMappingDto> urlDtos = getMappingDtos(urls);

            return new UrlPageResponse(urlDtos,pageNumber,pageSize,urlPages.getTotalElements(),urlPages.getTotalPages(),urlPages.isLast());
        } catch (IllegalArgumentException ex) {
            log.error("Invalid arguments provided for sorting/pagination: pageNumber={}, pageSize={}, sortBy={}, dir={}",
                    pageNumber, pageSize, sortBy, dir, ex);
            throw new PaginationException("Invalid pagination or sorting arguments provided", ex);
        } catch (Exception ex) {

            log.error("An error occurred during pagination and sorting: {}", ex.getMessage(), ex);
            throw new PaginationException("An error occurred while fetching paginated and sorted URLs", ex);
        }
    }

    private static List<UrlMappingDto> getMappingDtos(List<UrlMapping> urls) {
        List<UrlMappingDto> urlDtos = new ArrayList<>();
        for(UrlMapping url: urls){
            UrlMappingDto response = new UrlMappingDto(
                    url.getId(),
                    url.getShortUrl(),
                    url.getOriginalUrl(),
                    url.getCreatedAt(),
                    url.getExpiresAt(),
                    url.getUsageLimit(),
                    url.getUsageCount(),
                    url.getUser().getUserId()
            );
            urlDtos.add(response);
        }
        return urlDtos;
    }


    private static List<UrlMappingDto> getDto(List<UrlMapping> urls) {
        ArrayList<UrlMappingDto> urlMappingDto = new ArrayList<>();
        UrlMappingDto response;
        for (UrlMapping url : urls) {
            response = new UrlMappingDto(
                    url.getId(),
                    url.getOriginalUrl(),
                    url.getShortUrl(),
                    url.getCreatedAt(),
                    url.getExpiresAt(),
                    url.getUsageLimit(),
                    url.getUsageCount(),
                    url.getUser().getUserId()
            );
            urlMappingDto.add(response);
        }
        return urlMappingDto;
    }


    @Override
    public UrlResponse getUrlsByUser(User user) {
        UrlResponse urlResponse = new UrlResponse();
        try {
            log.info("Fetching all URL records for user: {}", user.getUserId());
            List<UrlMapping> urlRecords = urlMappingRepository.findByUser(user);

            if (urlRecords.isEmpty()) {
                log.warn("No URL records found for user: {}", user.getUserId());
                urlResponse.setMessage("No URL records found.");
                urlResponse.setStatus(HttpStatus.NOT_FOUND.value());
            } else {
                List<UrlMappingDto> urlMappingDtos = new ArrayList<>();
                for (UrlMapping urlMapping : urlRecords) {
                    urlMappingDtos.add(new UrlMappingDto(
                            urlMapping.getId(),
                            urlMapping.getOriginalUrl(),
                            urlMapping.getShortUrl(),
                            urlMapping.getCreatedAt(),
                            urlMapping.getExpiresAt(),
                            urlMapping.getUsageLimit(),
                            urlMapping.getUsageCount(),
                            urlMapping.getUser().getUserId()
                    ));
                }
                urlResponse.setData(urlMappingDtos);
                urlResponse.setMessage("URL records fetched successfully.");
                urlResponse.setStatus(HttpStatus.ACCEPTED.value());
            }
        } catch (Exception ex) {
            log.error("An error occurred while fetching URL records for user {}: {}", user.getUserId(), ex.getMessage());
            throw new RuntimeException("Error occurred while fetching URL records for user. Please try again.");
        }
        return urlResponse;
    }

}
