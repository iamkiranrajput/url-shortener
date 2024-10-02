package com.guardians.dto.ratingcomment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCommentRequest {
    private Long urlId;
    private Long userId;
    private String comment;
}
