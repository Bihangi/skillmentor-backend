package com.skillmentor.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreateReviewRequest {
    @NotNull private Long mentorId;
    private Long subjectId;
    @Min(1) @Max(5) private Integer rating;
    @NotBlank private String comment;
}
