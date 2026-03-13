package com.skillmentor.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class EnrollRequest {
    @NotNull(message = "Mentor ID is required")
    private Long mentorId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotBlank(message = "Session date is required")
    private String sessionDate; // ISO format: "2026-03-01"

    @NotBlank(message = "Session time is required")
    private String sessionTime; // "10:00"

    @Min(30) @Max(120)
    private Integer duration = 60;
}
