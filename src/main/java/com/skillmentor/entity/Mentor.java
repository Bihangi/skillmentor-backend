package com.skillmentor.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mentors")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Mentor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String profession;

    private String company;

    @Column(nullable = false)
    private Integer experienceYears;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String profileImageUrl;

    @Column(nullable = false)
    private Boolean isCertified = false;

    private Integer startYear;

    @Column(nullable = false)
    private Double rating = 0.0;

    @Column(nullable = false)
    private Integer reviewCount = 0;

    @Column(nullable = false)
    private Integer totalStudents = 0;

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Subject> subjects = new ArrayList<>();

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
