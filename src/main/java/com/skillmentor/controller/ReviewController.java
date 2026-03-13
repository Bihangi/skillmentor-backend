package com.skillmentor.controller;

import com.skillmentor.dto.*;
import com.skillmentor.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<List<ReviewDTO>> getByMentor(@PathVariable Long mentorId) {
        return ResponseEntity.ok(reviewService.getReviewsByMentor(mentorId));
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String clerkId = jwt.getSubject();
        String name = jwt.getClaimAsString("name");
        if (name == null) name = jwt.getClaimAsString("first_name") + " " + jwt.getClaimAsString("last_name");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.createReview(request, clerkId, name));
    }
}
