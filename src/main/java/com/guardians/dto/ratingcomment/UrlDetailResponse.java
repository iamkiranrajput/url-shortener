package com.guardians.dto.ratingcomment;


import com.guardians.dto.UrlMappingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrlDetailResponse {
    private UrlMappingDto urlMapping;
    private List<RatingDto> ratings;
    private List<CommentDto> comments;
    private String message;
    private int status;
}

