package com.guardians.dto.ratingcomment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRatingRequest {
    private Long urlId;
    private Long userId;
    private Long rating;

}
