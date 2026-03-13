package com.skillmentor.controller;

import com.skillmentor.dto.*;
import com.skillmentor.service.MentorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;

    @GetMapping
    public ResponseEntity<List<MentorDTO>> getAllMentors() {
        return ResponseEntity.ok(mentorService.getAllMentors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MentorDTO> getMentorById(@PathVariable Long id) {
        return ResponseEntity.ok(mentorService.getMentorById(id));
    }

    @PostMapping
    public ResponseEntity<MentorDTO> createMentor(@Valid @RequestBody CreateMentorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mentorService.createMentor(request));
    }
}
