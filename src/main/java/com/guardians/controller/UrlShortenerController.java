package com.guardians.controller;


import com.guardians.AppConstant;
import com.guardians.dto.UrlPageResponse;
import com.guardians.dto.UrlRequest;
import com.guardians.dto.UrlResponse;
import com.guardians.model.UrlMapping;
import com.guardians.service.UrlShortenerService;
import com.guardians.service.UrlShortenerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/url")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerServiceImpl urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> createShortUrl(@RequestBody UrlRequest urlRequest) {

        UrlResponse response = urlShortenerService.createShortUrl(urlRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<UrlResponse> redirectToOriginalUrl(@PathVariable String shortUrl) {
        UrlResponse response = urlShortenerService.getOriginalUrl(shortUrl);


        UrlMapping urlMapping = (UrlMapping) response.getData();
        if (urlMapping == null || urlMapping.getOriginalUrl() == null) {
            // If URL is expired or not found
            return new ResponseEntity<>(response, HttpStatus.GONE);
        }

        // Redirect to the original URL if found and valid
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(urlMapping.getOriginalUrl())).build();
    }

    @GetMapping("/all")
    public ResponseEntity<UrlResponse> getAllUrls() {
        UrlResponse response = urlShortenerService.getAllRecords();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/allUrlPages")
    public ResponseEntity<UrlPageResponse> getUrlWithPagination(@RequestParam(defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber, @RequestParam(defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize) {

        return ResponseEntity.ok(urlShortenerService.getAllUrlWithPagination(pageNumber, pageSize));
    }



    @GetMapping("/allUrlPagesSort")
    public ResponseEntity<UrlPageResponse> getUrlsWithPaginationAndSort(@RequestParam(defaultValue = AppConstant.PAGE_NUMBER,required = false) Integer pageNumber, @RequestParam(defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize, @RequestParam(defaultValue = AppConstant.SORT_DIR, required = false) String dir, @RequestParam(defaultValue = AppConstant.SORT_BY, required = false) String sortBy){

        return ResponseEntity.ok(urlShortenerService.getAllUrlWithPaginationAndSorting(pageNumber,pageSize,sortBy,dir));

    }

}
