package com.coreops.finance.auth;

import com.coreops.finance.auth.dto.LoginRequest;
import com.coreops.finance.auth.dto.LoginResponse;
import com.coreops.finance.auth.dto.RefreshRequest;
import com.coreops.finance.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.expiration}")
    private long expiration;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                user.getEmail(),
                user.getRole().name(),
                expiration
        );
    }

    public LoginResponse refresh(RefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(request.getRefreshToken());
        var user = refreshToken.getUser();

        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        String newAccessToken = jwtService.generateToken(userDetails);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        return new LoginResponse(
                newAccessToken,
                newRefreshToken.getToken(),
                user.getEmail(),
                user.getRole().name(),
                expiration
        );
    }
}