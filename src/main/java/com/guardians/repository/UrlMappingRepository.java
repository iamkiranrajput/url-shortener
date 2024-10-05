package com.guardians.repository;



import com.guardians.auth.entities.User;
import com.guardians.dto.UrlMappingDto;
import com.guardians.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByShortUrl(String shortUrl);
    Optional<UrlMapping> findById(int urlId);
    List<UrlMapping> findByUser(User user);


}
