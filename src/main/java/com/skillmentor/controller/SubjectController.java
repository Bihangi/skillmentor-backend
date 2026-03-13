package com.skillmentor.controller;

import com.skillmentor.dto.*;
import com.skillmentor.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<List<SubjectDTO>> getByMentor(@PathVariable Long mentorId) {
        return ResponseEntity.ok(subjectService.getSubjectsByMentor(mentorId));
    }

    @PostMapping
    public ResponseEntity<SubjectDTO> createSubject(@Valid @RequestBody CreateSubjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.createSubject(request));
    }
}
