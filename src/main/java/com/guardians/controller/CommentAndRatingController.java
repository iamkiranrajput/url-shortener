package com.guardians.controller;

import com.guardians.auth.entities.User;
import com.guardians.dto.ratingcomment.*;
import com.guardians.exception.AlreadyRatedException;
import com.guardians.model.Comment;
import com.guardians.service.CommentAndRatingService;
import com.guardians.service.UrlShortenerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("")
public class CommentAndRatingController {

    @Autowired
    CommentAndRatingService commentAndRatingService;

    @PostMapping("/ratings/{urlId}")
    public ResponseEntity<RatingDto> createRating(@PathVariable Long urlId, @RequestBody CreateRatingRequest request) throws AlreadyRatedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        log.info("Creating rating for URL ID: {}", urlId);
        request.setUrlId(urlId);
        request.setUserId(Long.valueOf(user.getUserId()));
        RatingDto ratingDto = commentAndRatingService.createRating(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingDto);
    }

    @PostMapping("/comments/{urlId}")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long urlId, @RequestBody CreateCommentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        log.info("Adding comment for URL ID: {}", urlId);
        request.setUrlId(urlId);
        request.setUserId(Long.valueOf(user.getUserId()));
        CommentDto commentDto = commentAndRatingService.createComment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
    }


    @GetMapping("/details/{urlId}")
    public ResponseEntity<UrlDetailResponse> getUrlDetails(@PathVariable Long urlId) {
        log.info("Fetching details for URL ID: {}", urlId);
        UrlDetailResponse response = commentAndRatingService.getUrlDetails(urlId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/ratings/{urlId}")
    public ResponseEntity<List<RatingDto>> getUrlRating(@PathVariable Long urlId){
        log.info("Fetching Rating of Url ID: {}", urlId);
        List<RatingDto> rating = commentAndRatingService.getRatingsByUrl(urlId);
        return new ResponseEntity<>(rating, HttpStatus.OK);
    }

    @GetMapping("/comments/{urlId}")
    public ResponseEntity<List<CommentDto>> getUrlComment(@PathVariable Long urlId){
        log.info("Fetching Comment of Url ID: {}", urlId);
        List<CommentDto>comment = commentAndRatingService.getCommentsByUrl(urlId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

}
