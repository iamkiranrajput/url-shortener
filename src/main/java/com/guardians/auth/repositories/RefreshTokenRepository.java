package com.guardians.auth.repositories;

import com.guardians.auth.entities.RefreshToken;
import com.guardians.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {


    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    Optional<RefreshToken> findByUser(User user);

}
