package com.guardians.controller;


import com.guardians.dto.UrlRequest;
import com.guardians.dto.UrlMappingDto;
import com.guardians.model.UrlMapping;
import com.guardians.service.UrlShortenerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/url")
public class UrlShortenerController {

    private final UrlShortenerServiceImpl urlShortenerService;

    public UrlShortenerController(UrlShortenerServiceImpl urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<UrlMapping> createShortUrl(@RequestBody UrlRequest urlRequest) {
        // Use the data from the request body
        UrlMapping urlMapping = urlShortenerService.createShortUrl(urlRequest);
        return ResponseEntity.ok(urlMapping);
    }
    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortUrl) {
        Optional<UrlMappingDto> urlMappingOptional = urlShortenerService.getOriginalUrl(shortUrl);

        if (urlMappingOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UrlMappingDto urlResponseDTO = urlMappingOptional.get();
        if (urlShortenerService.isUrlExpiredOrLimitReached(urlResponseDTO)) {
            return ResponseEntity.status(HttpStatus.GONE).build(); // 410 Gone if the URL is expired or limit reached
        }



        urlShortenerService.incrementUsageCount(urlResponseDTO);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(urlResponseDTO.getOriginalUrl())).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<UrlMappingDto>> getAllUrls(){

        List response = urlShortenerService.getAllRecord();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
