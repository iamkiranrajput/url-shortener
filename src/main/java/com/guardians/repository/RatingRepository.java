package com.guardians.repository;

import com.guardians.dto.ratingcomment.RatingDto;
import com.guardians.model.Comment;
import com.guardians.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByUrlId(Long urlMappingId);

    Optional<Rating> findByUserIdAndUrlId(Long userId, Long urlId);
}
