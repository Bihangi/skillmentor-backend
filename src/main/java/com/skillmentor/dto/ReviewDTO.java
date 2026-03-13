package com.skillmentor.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewDTO {
    private Long id;
    private String studentName;
    private Integer rating;
    private String comment;
    private String date;
    private Long mentorId;
    private Long subjectId;
}
