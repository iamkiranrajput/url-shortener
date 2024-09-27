package com.guardians.controller;

import com.guardians.auth.entities.RefreshToken;
import com.guardians.auth.entities.User;
import com.guardians.auth.service.AuthService;
import com.guardians.auth.service.JwtService;
import com.guardians.auth.service.RefreshTokenService;
import com.guardians.auth.utils.AuthResponse;
import com.guardians.auth.utils.LoginRequest;
import com.guardians.auth.utils.RefreshTokenRequest;
import com.guardians.auth.utils.RegisterRequest;
import com.guardians.exception.UserAlreadyExistsException;
import com.guardians.exception.UsernameNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;


    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) throws UserAlreadyExistsException {
      return ResponseEntity.ok(authService.register(registerRequest));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) throws UsernameNotFoundException {
        return ResponseEntity.ok(authService.login(loginRequest));
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());

        User user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(AuthResponse.builder().accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken()).build());

    }
}
