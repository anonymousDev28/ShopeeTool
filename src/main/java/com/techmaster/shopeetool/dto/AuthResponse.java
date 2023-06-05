package com.techmaster.shopeetool.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private UserDto auth;
    private String token;
    private String refreshToken;
    @JsonProperty("isAuthenticated")
    private Boolean isAuthenticated;
}
