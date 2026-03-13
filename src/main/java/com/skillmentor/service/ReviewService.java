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
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MentorRepository mentorRepository;
    private final SubjectRepository subjectRepository;

    public List<ReviewDTO> getReviewsByMentor(Long mentorId) {
        return reviewRepository.findByMentorIdOrderByCreatedAtDesc(mentorId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDTO createReview(CreateReviewRequest request, String studentClerkId, String studentName) {
        Mentor mentor = mentorRepository.findById(request.getMentorId())
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));

        Subject subject = null;
        if (request.getSubjectId() != null) {
            subject = subjectRepository.findById(request.getSubjectId()).orElse(null);
        }

        Review review = Review.builder()
                .studentClerkId(studentClerkId)
                .studentName(studentName)
                .mentor(mentor)
                .subject(subject)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review saved = reviewRepository.save(review);

        // Update mentor rating and review count
        Double avgRating = reviewRepository.findAverageRatingByMentorId(mentor.getId());
        long count = reviewRepository.countByMentorId(mentor.getId());
        mentor.setRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0);
        mentor.setReviewCount((int) count);
        mentorRepository.save(mentor);

        return toDTO(saved);
    }

    private ReviewDTO toDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .studentName(review.getStudentName())
                .rating(review.getRating())
                .comment(review.getComment())
                .date(review.getCreatedAt().toLocalDate().toString())
                .mentorId(review.getMentor().getId())
                .subjectId(review.getSubject() != null ? review.getSubject().getId() : null)
                .build();
    }
}
