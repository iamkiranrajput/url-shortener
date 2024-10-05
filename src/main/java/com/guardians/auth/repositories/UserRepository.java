package com.guardians.auth.repositories;

import com.guardians.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);
    Optional<User> findByUsername(String username);


    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}