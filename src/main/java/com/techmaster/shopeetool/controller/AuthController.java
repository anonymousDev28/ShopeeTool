package com.techmaster.shopeetool.controller;

import com.techmaster.shopeetool.dto.AuthResponse;
import com.techmaster.shopeetool.mapper.UserMapper;
import com.techmaster.shopeetool.model.User;
import com.techmaster.shopeetool.repository.UserRepository;
import com.techmaster.shopeetool.request.LoginRequest;
import com.techmaster.shopeetool.request.RefreshTokenRequest;
import com.techmaster.shopeetool.security.CustomUserDetailsService;
import com.techmaster.shopeetool.security.JwtUtils;
import com.techmaster.shopeetool.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationService service;
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
//        // Tạo đối tượng xác thực
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
//                request.getEmail(),
//                request.getPassword()
//        );
//
//        try {
//            // Tiến hành xác thực
//            Authentication authentication = authenticationManager.authenticate(token);
//
//            // Lưu dữ liệu vào context
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            // TODO: Tạo ra token -> trả về thông tin sau khi đăng nhập
//            UserDetails userDetails = customUserDetailsService.loadUserByUsername(authentication.getName());
//
//            // tao token
//            String jwtToken = jwtUtils.generateToken(userDetails);
//            String refreshToken = jwtUtils.generateRefreshToken(userDetails);
//            User user = userRepository.findByEmail(authentication.getName()).orElse(null);
//
//            return ResponseEntity.ok(new AuthResponse(
//                    UserMapper.toUserDto(user),
//                    jwtToken,
//                    refreshToken,
//                    true
//            ));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
        AuthResponse response = service.authenticate(request);
        return response.getIsAuthenticated()?
                ResponseEntity.ok(response):
                ResponseEntity.badRequest().body("username or password is invalid !");
    }
    @PostMapping("refresh-token")
    public AuthResponse getRefreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return service.refreshToken(request, response);
    }
}
