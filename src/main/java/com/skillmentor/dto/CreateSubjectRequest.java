package com.skillmentor.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreateSubjectRequest {
    @NotBlank(message = "Subject name is required")
    private String name;

    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    @NotNull(message = "Mentor ID is required")
    private Long mentorId;
}
