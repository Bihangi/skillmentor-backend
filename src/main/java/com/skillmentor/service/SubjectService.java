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
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final MentorRepository mentorRepository;

    public List<SubjectDTO> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<SubjectDTO> getSubjectsByMentor(Long mentorId) {
        return subjectRepository.findByMentorId(mentorId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubjectDTO createSubject(CreateSubjectRequest request) {
        Mentor mentor = mentorRepository.findById(request.getMentorId())
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found with id: " + request.getMentorId()));

        Subject subject = Subject.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .enrollmentCount(0)
                .mentor(mentor)
                .build();

        return toDTO(subjectRepository.save(subject));
    }

    private SubjectDTO toDTO(Subject subject) {
        return SubjectDTO.builder()
                .id(subject.getId())
                .name(subject.getName())
                .description(subject.getDescription())
                .imageUrl(subject.getImageUrl())
                .enrollmentCount(subject.getEnrollmentCount())
                .mentorId(subject.getMentor().getId())
                .mentorName(subject.getMentor().getFirstName() + " " + subject.getMentor().getLastName())
                .build();
    }
}
