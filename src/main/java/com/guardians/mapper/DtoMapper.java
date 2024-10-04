package com.guardians.mapper;
import com.guardians.dto.UrlMappingDto;
import com.guardians.dto.ratingcomment.CommentDto;
import com.guardians.dto.ratingcomment.RatingDto;
import com.guardians.model.Comment;
import com.guardians.model.Rating;
import com.guardians.model.UrlMapping;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public CommentDto convertToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setUrlId(comment.getUrlId());
        commentDto.setUserId(comment.getUserId());
        commentDto.setComment(comment.getComment());
        commentDto.setCreatedAt(comment.getCreatedAt());
        return commentDto;
    }

    public RatingDto convertToRatingDto(Rating rating) {
        RatingDto ratingDto = new RatingDto();
        ratingDto.setId(rating.getId());
        ratingDto.setUrlId(rating.getUrlId());
        ratingDto.setUserId(rating.getUserId());
        ratingDto.setRating(rating.getRating());
        ratingDto.setCreatedAt(rating.getCreatedAt());
        return ratingDto;
    }

    public UrlMappingDto convertToUrlMappingDto(UrlMapping urlMapping) {
        UrlMappingDto urlMappingDto = new UrlMappingDto();
        urlMappingDto.setId(urlMapping.getId());
        urlMappingDto.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDto.setShortUrl(urlMapping.getShortUrl());
        urlMappingDto.setCreatedAt(urlMapping.getCreatedAt());
        urlMappingDto.setExpiresAt(urlMapping.getExpiresAt());
        urlMappingDto.setUsageLimit(urlMapping.getUsageLimit());
        urlMappingDto.setUsageCount(urlMapping.getUsageCount());
        urlMappingDto.setUserId(urlMapping.getUser().getUserId());
        return urlMappingDto;
    }
}
