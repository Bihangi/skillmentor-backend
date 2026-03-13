package com.skillmentor.service;

import com.skillmentor.dto.*;
import com.skillmentor.entity.*;
import com.skillmentor.exception.*;
import com.skillmentor.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final MentorRepository mentorRepository;
    private final SubjectRepository subjectRepository;
    private final EmailService emailService;

    @Value("${app.upload.dir:uploads/payment-proofs}")
    private String uploadDir;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public List<SessionDTO> getStudentSessions(String studentClerkId) {
        return sessionRepository.findByStudentClerkIdOrderBySessionDateDesc(studentClerkId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<SessionDTO> getAllSessions() {
        return sessionRepository.findAllByOrderBySessionDateDesc().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SessionDTO enrollSession(EnrollRequest request, String studentClerkId, String studentName, String studentEmail) {
        Mentor mentor = mentorRepository.findById(request.getMentorId())
                .orElseThrow(() -> new ResourceNotFoundException("Mentor not found"));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        LocalDate date = LocalDate.parse(request.getSessionDate());
        LocalTime time = LocalTime.parse(request.getSessionTime());
        LocalDateTime sessionDateTime = LocalDateTime.of(date, time);
        LocalDateTime endDateTime = sessionDateTime.plusMinutes(request.getDuration());

        if (sessionDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot book a session in the past");
        }

        List<Session> overlapping = sessionRepository.findOverlappingSessions(
                studentClerkId, request.getMentorId(), sessionDateTime, endDateTime);
        if (!overlapping.isEmpty()) {
            throw new BookingConflictException("You already have a session with this mentor at an overlapping time slot");
        }

        List<Session> duplicates = sessionRepository.findDuplicateSubjectBookings(
                studentClerkId, request.getSubjectId(), sessionDateTime, endDateTime);
        if (!duplicates.isEmpty()) {
            throw new BookingConflictException("You already have a session for this subject at an overlapping time");
        }

        Session session = Session.builder()
                .studentClerkId(studentClerkId)
                .studentName(studentName)
                .studentEmail(studentEmail)
                .mentor(mentor)
                .subject(subject)
                .sessionDate(sessionDateTime)
                .duration(request.getDuration())
                .paymentStatus(Session.PaymentStatus.PENDING)
                .sessionStatus(Session.SessionStatus.PENDING)
                .build();

        Session saved = sessionRepository.save(session);

        subject.setEnrollmentCount(subject.getEnrollmentCount() + 1);
        subjectRepository.save(subject);

        mentor.setTotalStudents(mentor.getTotalStudents() + 1);
        mentorRepository.save(mentor);

        // Send email notifications
        emailService.sendBookingConfirmationToStudent(saved);
        // If mentor entity has an email field, pass it here; otherwise null skips the email
        emailService.sendBookingNotificationToMentor(saved, null);

        return toDTO(saved);
    }

    @Transactional
    public SessionDTO updatePaymentStatus(Long sessionId, String status) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        session.setPaymentStatus(Session.PaymentStatus.valueOf(status.toUpperCase()));
        if (session.getPaymentStatus() == Session.PaymentStatus.CONFIRMED) {
            session.setSessionStatus(Session.SessionStatus.CONFIRMED);
        }
        Session saved = sessionRepository.save(session);

        // Notify student when payment is confirmed
        if (saved.getPaymentStatus() == Session.PaymentStatus.CONFIRMED) {
            emailService.sendPaymentConfirmedToStudent(saved);
        }

        return toDTO(saved);
    }

    @Transactional
    public SessionDTO updateSessionStatus(Long sessionId, String status) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        Session.SessionStatus newStatus = Session.SessionStatus.valueOf(status.toUpperCase());
        session.setSessionStatus(newStatus);
        Session saved = sessionRepository.save(session);

        // Send notifications based on status change
        if (newStatus == Session.SessionStatus.COMPLETED) {
            emailService.sendSessionCompletedToStudent(saved);
        } else if (newStatus == Session.SessionStatus.CANCELLED) {
            emailService.sendSessionCancelledNotification(saved);
        }

        return toDTO(saved);
    }

    @Transactional
    public SessionDTO addMeetingLink(Long sessionId, String meetingLink) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        session.setMeetingLink(meetingLink);
        return toDTO(sessionRepository.save(session));
    }

    @Transactional
    public SessionDTO uploadPaymentProof(Long sessionId, MultipartFile file, String studentClerkId) throws IOException {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        // Verify the student owns this session
        if (!session.getStudentClerkId().equals(studentClerkId)) {
            throw new IllegalArgumentException("You can only upload payment proof for your own sessions");
        }

        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be under 5MB");
        }

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".png";
        String filename = UUID.randomUUID() + extension;

        // Save file
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Update session with proof URL
        String proofUrl = baseUrl + "/uploads/payment-proofs/" + filename;
        session.setPaymentProofUrl(proofUrl);
        session.setPaymentStatus(Session.PaymentStatus.PENDING); // stays pending until admin confirms

        return toDTO(sessionRepository.save(session));
    }

    @Transactional
    public SessionDTO requestReschedule(Long sessionId, String newDate, String newTime, String reason, String studentClerkId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        if (!session.getStudentClerkId().equals(studentClerkId)) {
            throw new IllegalArgumentException("You can only reschedule your own sessions");
        }

        if (session.getSessionStatus() == Session.SessionStatus.COMPLETED || session.getSessionStatus() == Session.SessionStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot reschedule a " + session.getSessionStatus().name().toLowerCase() + " session");
        }

        LocalDateTime requestedDateTime = LocalDateTime.of(LocalDate.parse(newDate), LocalTime.parse(newTime));
        if (requestedDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot reschedule to a past date/time");
        }

        session.setRequestedNewDate(requestedDateTime);
        session.setRescheduleReason(reason);
        session.setRescheduleStatus(Session.RescheduleStatus.PENDING);

        return toDTO(sessionRepository.save(session));
    }

    @Transactional
    public SessionDTO approveReschedule(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        if (session.getRescheduleStatus() != Session.RescheduleStatus.PENDING) {
            throw new IllegalArgumentException("No pending reschedule request");
        }

        session.setSessionDate(session.getRequestedNewDate());
        session.setRescheduleStatus(Session.RescheduleStatus.APPROVED);
        session.setRequestedNewDate(null);
        session.setRescheduleReason(null);

        Session saved = sessionRepository.save(session);
        emailService.sendPaymentConfirmedToStudent(saved); // reuse to notify student of update
        return toDTO(saved);
    }

    @Transactional
    public SessionDTO rejectReschedule(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        if (session.getRescheduleStatus() != Session.RescheduleStatus.PENDING) {
            throw new IllegalArgumentException("No pending reschedule request");
        }

        session.setRescheduleStatus(Session.RescheduleStatus.REJECTED);
        session.setRequestedNewDate(null);
        session.setRescheduleReason(null);

        return toDTO(sessionRepository.save(session));
    }

    private SessionDTO toDTO(Session session) {
        SessionDTO.SessionDTOBuilder builder = SessionDTO.builder()
                .id(session.getId())
                .studentName(session.getStudentName())
                .studentEmail(session.getStudentEmail())
                .mentorName(session.getMentor().getFirstName() + " " + session.getMentor().getLastName())
                .subjectName(session.getSubject().getName())
                .mentorId(session.getMentor().getId())
                .subjectId(session.getSubject().getId())
                .sessionDate(session.getSessionDate().toLocalDate().toString())
                .sessionTime(session.getSessionDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .duration(session.getDuration())
                .paymentStatus(session.getPaymentStatus().name().toLowerCase())
                .sessionStatus(session.getSessionStatus().name().toLowerCase())
                .meetingLink(session.getMeetingLink())
                .paymentProofUrl(session.getPaymentProofUrl());

        if (session.getRequestedNewDate() != null) {
            builder.requestedNewDate(session.getRequestedNewDate().toLocalDate().toString())
                    .requestedNewTime(session.getRequestedNewDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        }
        if (session.getRescheduleReason() != null) {
            builder.rescheduleReason(session.getRescheduleReason());
        }
        if (session.getRescheduleStatus() != null) {
            builder.rescheduleStatus(session.getRescheduleStatus().name().toLowerCase());
        }

        return builder.build();
    }
}
