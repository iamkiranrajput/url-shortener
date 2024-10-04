package com.guardians.dto.ratingcomment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {
    private Long id;
    private Long urlId;
    private Long userId;
    private Long rating;
    private LocalDateTime createdAt;
}
