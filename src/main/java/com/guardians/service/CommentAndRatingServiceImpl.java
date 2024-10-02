package com.guardians.service;


import com.guardians.dto.UrlMappingDto;
import com.guardians.dto.ratingcomment.*;
import com.guardians.exception.AlreadyRatedException;
import com.guardians.mapper.DtoMapper;
import com.guardians.model.Comment;
import com.guardians.model.Rating;
import com.guardians.model.UrlMapping;
import com.guardians.repository.CommentRepository;
import com.guardians.repository.RatingRepository;
import com.guardians.repository.UrlMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentAndRatingServiceImpl implements CommentAndRatingService {

    @Autowired
    DtoMapper mapper;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private UrlMappingRepository urlMappingRepository;
    public CommentDto createComment(CreateCommentRequest request) {
        try {
            Comment comment = new Comment();
            comment.setUrlId(request.getUrlId());
            comment.setUserId(request.getUserId());
            comment.setComment(request.getComment());
            comment.setCreatedAt(LocalDateTime.now());
            comment = commentRepository.save(comment);
            log.info("Comment created successfully: {}", comment);
            return mapper.convertToCommentDto(comment);
        } catch (Exception e) {
            log.error("Error creating comment: {}", e.getMessage());
            throw new RuntimeException("Error creating comment. Please try again later.");
        }
    }

    public UrlDetailResponse getUrlDetails(Long urlMappingId) {
        UrlDetailResponse response = new UrlDetailResponse();
        try {
            Optional<UrlMapping> optionalUrlMapping = urlMappingRepository.findById(urlMappingId);
            if (optionalUrlMapping.isPresent()) {
                UrlMapping urlMapping = optionalUrlMapping.get();
                List<RatingDto> ratings = getRatingsByUrl(urlMappingId);
                List<CommentDto> comments = getCommentsByUrl(urlMappingId);
                UrlMappingDto urlMappingDto = mapper.convertToUrlMappingDto(urlMapping);

                response.setUrlMapping(urlMappingDto);
                response.setRatings(ratings);
                response.setComments(comments);
                response.setMessage("URL details fetched successfully.");
                response.setStatus(HttpStatus.OK.value());
                log.info("Fetched details for URL ID: {}", urlMappingId);
            } else {
                response.setMessage("URL mapping not found.");
                response.setStatus(HttpStatus.NOT_FOUND.value());
                log.warn("No URL mapping found for ID: {}", urlMappingId);
            }
        } catch (Exception e) {
            log.error("Error fetching URL details: {}", e.getMessage());
            throw new RuntimeException("Error fetching URL details. Please try again later.");
        }
        return response;
    }


    public RatingDto createRating(CreateRatingRequest request) throws AlreadyRatedException {
        try {
            // Check if user has already rated the URL
            Optional<Rating> existingRating = ratingRepository.findByUserIdAndUrlId(request.getUserId(), request.getUrlId());
            if (existingRating.isPresent()) {
                log.warn("User {} has already rated URL {}", request.getUserId(), request.getUrlId());
                throw new AlreadyRatedException("You have already rated this URL.");
            }

            // Retrieve URL mapping or throw an error if not found
            UrlMapping urlMapping = urlMappingRepository.findById(request.getUrlId())
                    .orElseThrow(() -> new RuntimeException("URL not found."));

            // Create a new Rating object
            Rating rating = new Rating();
            rating.setUrlId(urlMapping.getId());
            rating.setUserId(request.getUserId());
            rating.setRating(request.getRating());
            rating.setCreatedAt(LocalDateTime.now());

            // Save the new rating and log the success
            rating = ratingRepository.save(rating);
            log.info("Rating submitted successfully: {}", rating);

            // Return the saved rating as DTO
            return mapper.convertToRatingDto(rating);

        } catch (AlreadyRatedException e) {
            // Handle case when the user has already rated the URL
            log.warn("Rating submission failed: {}", e.getMessage());
            throw e;  // Re-throw AlreadyRatedException if needed for higher-level handling

        }
    }


    @Override
    public List<RatingDto> getRatingsByUrl(Long urlId) {
        try {
            log.info("Fetching ratings for URL ID: {}", urlId);
            List<Rating> ratings = ratingRepository.findByUrlId(urlId);
            if (ratings.isEmpty()) log.warn("No ratings found for URL ID: {}", urlId);
            List<RatingDto> ratingDtos = ratings.stream()
                    .map(mapper::convertToRatingDto)
                    .collect(Collectors.toList());
            log.info("Fetched {} ratings for URL ID: {}", ratingDtos.size(), urlId);
            return ratingDtos;
        } catch (Exception e) {
            log.error("Error fetching ratings for URL ID: {}: {}", urlId, e.getMessage());
            throw new RuntimeException("Error fetching ratings for URL. Please try again later.");
        }
    }

    @Override
    public List<CommentDto> getCommentsByUrl(Long urlId) {
        try {
            log.info("Fetching comments for URL ID: {}", urlId);
            List<Comment> comments = commentRepository.findByUrlId(urlId);
            if (comments.isEmpty()) log.warn("No comments found for URL ID: {}", urlId);
            List<CommentDto> commentDtos = comments.stream()
                    .map(mapper::convertToCommentDto)
                    .collect(Collectors.toList());
            log.info("Fetched {} comments for URL ID: {}", commentDtos.size(), urlId);
            return commentDtos;
        } catch (Exception e) {
            log.error("Error fetching comments for URL ID: {}: {}", urlId, e.getMessage());
            throw new RuntimeException("Error fetching comments for URL. Please try again later.");
        }
    }


}
