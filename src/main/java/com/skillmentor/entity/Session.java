package com.skillmentor.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String studentClerkId;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String studentEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private LocalDateTime sessionDate;

    @Column(nullable = false)
    private Integer duration; // in minutes

    private String meetingLink;

    private String paymentProofUrl;

    // Reschedule fields
    private LocalDateTime requestedNewDate;
    private String rescheduleReason;

    @Enumerated(EnumType.STRING)
    private RescheduleStatus rescheduleStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus sessionStatus = SessionStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum PaymentStatus {
        PENDING, CONFIRMED, REJECTED
    }

    public enum SessionStatus {
        PENDING, CONFIRMED, COMPLETED, CANCELLED
    }

    public enum RescheduleStatus {
        PENDING, APPROVED, REJECTED
    }
}
