package com.guardians.repository;

import com.guardians.dto.ratingcomment.CommentDto;
import com.guardians.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByUrlId(Long urlMappingId);

}
