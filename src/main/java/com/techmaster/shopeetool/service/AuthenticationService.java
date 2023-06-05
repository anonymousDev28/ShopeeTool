package com.techmaster.shopeetool.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techmaster.shopeetool.dto.AuthResponse;
import com.techmaster.shopeetool.mapper.UserMapper;
import com.techmaster.shopeetool.model.RefreshToken;
import com.techmaster.shopeetool.model.User;
import com.techmaster.shopeetool.repository.RefreshTokenRepository;
import com.techmaster.shopeetool.repository.UserRepository;
import com.techmaster.shopeetool.request.LoginRequest;
import com.techmaster.shopeetool.security.CustomUserDetailsService;
import com.techmaster.shopeetool.security.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Component
public class AuthenticationService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserRepository userRepository;
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    public AuthResponse authenticate(LoginRequest request){

        // Tạo đối tượng xác thực
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        try {
            // Tiến hành xác thực
            Authentication authentication = authenticationManager.authenticate(token);

            // Lưu dữ liệu vào context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // TODO: Tạo ra token -> trả về thông tin sau khi đăng nhập
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(authentication.getName());

            // tao token
            String jwtToken = jwtUtils.generateToken(userDetails);
            String refreshToken = jwtUtils.createRefreshToken(userDetails).getToken();
            User user = userRepository.findByEmail(authentication.getName()).orElse(null);

            return new AuthResponse(
                    UserMapper.toUserDto(user),
                    jwtToken,
                    refreshToken,
                    true
            );
        } catch (Exception e) {
            return new AuthResponse(null,null,null,false);
        }
    }

    public AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        AuthResponse refreshResponse = new AuthResponse(null, null, null, false);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return refreshResponse;
        } else {
            refreshToken = authHeader.substring(7);
            userEmail = jwtUtils.extractUsername(refreshToken);
            if (userEmail != null) {
                var user = userRepository.findByEmail(userEmail)
                        .orElseThrow();
                if (jwtUtils.isTokenValid(refreshToken, user)) {
                    var accessToken = jwtUtils.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                    var authResponse = AuthResponse.builder()
//                            .token(accessToken)
//                            .refreshToken(refreshToken)
//                            .build();
//                    return authResponse;
                    refreshResponse = AuthResponse.builder()
                            .token(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                }
            }
        }
        return refreshResponse;
    }
}
