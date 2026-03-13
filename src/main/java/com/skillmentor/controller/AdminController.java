package com.skillmentor.controller;

import com.skillmentor.dto.*;
import com.skillmentor.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MentorService mentorService;
    private final SubjectService subjectService;
    private final SessionService sessionService;

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getOverview() {
        List<MentorDTO> mentors = mentorService.getAllMentors();
        List<SubjectDTO> subjects = subjectService.getAllSubjects();
        List<SessionDTO> sessions = sessionService.getAllSessions();

        long pendingSessions = sessions.stream()
                .filter(s -> "pending".equals(s.getSessionStatus()))
                .count();
        long confirmedSessions = sessions.stream()
                .filter(s -> "confirmed".equals(s.getSessionStatus()))
                .count();

        Map<String, Object> overview = new HashMap<>();
        overview.put("totalMentors", mentors.size());
        overview.put("totalSubjects", subjects.size());
        overview.put("totalBookings", sessions.size());
        overview.put("pendingBookings", pendingSessions);
        overview.put("confirmedBookings", confirmedSessions);

        return ResponseEntity.ok(overview);
    }
}
