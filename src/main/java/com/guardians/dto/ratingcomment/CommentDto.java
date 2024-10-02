package com.guardians.dto.ratingcomment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private Long urlId;
    private Long userId;
    private String comment;
    private LocalDateTime createdAt;

}
