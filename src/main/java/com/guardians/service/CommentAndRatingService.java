package com.guardians.service;

import com.guardians.dto.ratingcomment.*;
import com.guardians.exception.AlreadyRatedException;

import java.util.List;

public interface CommentAndRatingService {
    CommentDto createComment(CreateCommentRequest request);
    UrlDetailResponse getUrlDetails(Long urlId);
    RatingDto createRating(CreateRatingRequest request) throws AlreadyRatedException;
    List<RatingDto> getRatingsByUrl(Long urlId);
    List<CommentDto> getCommentsByUrl(Long urlId);
}
