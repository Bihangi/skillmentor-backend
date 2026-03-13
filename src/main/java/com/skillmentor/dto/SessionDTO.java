package com.skillmentor.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SessionDTO {
    private Long id;
    private String studentName;
    private String studentEmail;
    private String mentorName;
    private String subjectName;
    private Long mentorId;
    private Long subjectId;
    private String sessionDate;
    private String sessionTime;
    private Integer duration;
    private String paymentStatus;
    private String sessionStatus;
    private String meetingLink;
    private String paymentProofUrl;
    private String requestedNewDate;
    private String requestedNewTime;
    private String rescheduleReason;
    private String rescheduleStatus;
}
