package com.skillmentor.service;

import com.skillmentor.dto.*;
import com.skillmentor.entity.*;
import com.skillmentor.exception.ResourceNotFoundException;
import com.skillmentor.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentorService {

    private final MentorRepository mentorRepository;
    private final ReviewRepository reviewRepository;

    public List<MentorDTO> getAllMentors() {
        return mentorRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MentorDTO getMentorById(Long id) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with id: " + id));
        return toDTO(mentor);
    }

    @Transactional
    public MentorDTO createMentor(CreateMentorRequest request) {
        if (mentorRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("A mentor with this email already exists");
        }

        Mentor mentor = Mentor.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .title(request.getTitle())
                .profession(request.getProfession())
                .company(request.getCompany())
                .experienceYears(request.getExperienceYears())
                .bio(request.getBio())
                .profileImageUrl(request.getProfileImageUrl())
                .isCertified(request.getIsCertified() != null ? request.getIsCertified() : false)
                .startYear(request.getStartYear())
                .rating(0.0)
                .reviewCount(0)
                .totalStudents(0)
                .build();

        return toDTO(mentorRepository.save(mentor));
    }

    private MentorDTO toDTO(Mentor mentor) {
        List<SubjectDTO> subjectDTOs = mentor.getSubjects().stream()
                .map(s -> SubjectDTO.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .description(s.getDescription())
                        .imageUrl(s.getImageUrl())
                        .enrollmentCount(s.getEnrollmentCount())
                        .mentorId(mentor.getId())
                        .build())
                .collect(Collectors.toList());

        return MentorDTO.builder()
                .id(mentor.getId())
                .firstName(mentor.getFirstName())
                .lastName(mentor.getLastName())
                .email(mentor.getEmail())
                .phone(mentor.getPhone())
                .title(mentor.getTitle())
                .profession(mentor.getProfession())
                .company(mentor.getCompany())
                .experienceYears(mentor.getExperienceYears())
                .bio(mentor.getBio())
                .profileImageUrl(mentor.getProfileImageUrl())
                .isCertified(mentor.getIsCertified())
                .startYear(mentor.getStartYear())
                .rating(mentor.getRating())
                .reviewCount(mentor.getReviewCount())
                .totalStudents(mentor.getTotalStudents())
                .subjects(subjectDTOs)
                .build();
    }
}
