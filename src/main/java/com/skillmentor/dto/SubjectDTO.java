package com.skillmentor.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SubjectDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer enrollmentCount;
    private Long mentorId;
    private String mentorName;
}
