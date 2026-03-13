package com.skillmentor.dto;

import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MentorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String title;
    private String profession;
    private String company;
    private Integer experienceYears;
    private String bio;
    private String profileImageUrl;
    private Boolean isCertified;
    private Integer startYear;
    private Double rating;
    private Integer reviewCount;
    private Integer totalStudents;
    private List<SubjectDTO> subjects;
}
