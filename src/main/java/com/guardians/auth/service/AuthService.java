package com.guardians.auth.service;

import com.guardians.auth.entities.User;
import com.guardians.auth.entities.UserRole;
import com.guardians.auth.repositories.UserRepository;
import com.guardians.auth.utils.AuthResponse;
import com.guardians.auth.utils.LoginRequest;
import com.guardians.auth.utils.RegisterRequest;
import com.guardians.exception.UserAlreadyExistsException;
import com.guardians.exception.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest) throws UserAlreadyExistsException {
        // Check if user with the same username or email already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + registerRequest.getUsername());
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + registerRequest.getEmail());
        }

        User user = User.builder()
                .name(registerRequest.getName())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER) // or whatever default role you want
                .isEnabled(true) // Ensure enabled
                .isAccountNonLocked(true) // Ensure account is not locked
                .isCredentialsNonExpired(true) // Credentials are not expired
                .isAccountNonExpired(true) // Account is not expired
                .build();

        User savedUser = userRepository.save(user);
        var accessToken = jwtService.generateToken(savedUser);
        var refreshToken = refreshTokenService.createRefreshToken(savedUser.getUsername());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest) throws UsernameNotFoundException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        var userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isPresent()) {
            var user = userOptional.get();
            var accessToken = jwtService.generateToken(user);
            var refreshToken = refreshTokenService.createRefreshToken(loginRequest.getUsername());

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getRefreshToken())
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found!");
        }
    }
}

