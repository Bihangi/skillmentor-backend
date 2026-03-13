package com.skillmentor.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    @Value("${clerk.secret-key:}")
    private String clerkSecretKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @PutMapping("/role")
    public ResponseEntity<?> setRole(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Map<String, String> body
    ) {
        String role = body.get("role");
        if (role == null || (!role.equals("student") && !role.equals("mentor"))) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid role. Must be 'student' or 'mentor'."));
        }

        String userId = jwt.getSubject();

        // Update Clerk user public metadata via Clerk Backend API
        String url = "https://api.clerk.com/v1/users/" + userId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(clerkSecretKey);

        Map<String, Object> payload = Map.of(
                "public_metadata", Map.of("role", role)
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            restTemplate.exchange(url, HttpMethod.PATCH, request, String.class);
            return ResponseEntity.ok(Map.of("message", "Role set to " + role));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update role: " + e.getMessage()));
        }
    }
}
