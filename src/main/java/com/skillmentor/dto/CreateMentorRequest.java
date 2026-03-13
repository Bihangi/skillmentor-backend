package com.skillmentor.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreateMentorRequest {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank @Email(message = "Valid email required")
    private String email;

    private String phone;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Profession is required")
    private String profession;

    private String company;

    @Min(0) @Max(50)
    private Integer experienceYears;

    @Size(min = 20, message = "Bio must be at least 20 characters")
    private String bio;

    @NotBlank(message = "Profile image URL is required")
    private String profileImageUrl;

    private Boolean isCertified = false;

    @Min(2000)
    private Integer startYear;
}
