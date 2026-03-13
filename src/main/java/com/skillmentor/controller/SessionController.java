package com.skillmentor.controller;

import com.skillmentor.dto.*;
import com.skillmentor.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/enroll")
    public ResponseEntity<SessionDTO> enroll(
            @Valid @RequestBody EnrollRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String clerkId = jwt.getSubject();
        String name = jwt.getClaimAsString("name");
        String email = jwt.getClaimAsString("email");
        if (name == null) name = jwt.getClaimAsString("first_name") + " " + jwt.getClaimAsString("last_name");
        if (email == null) email = "";

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sessionService.enrollSession(request, clerkId, name, email));
    }

    @GetMapping("/my-sessions")
    public ResponseEntity<List<SessionDTO>> getMySessions(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(sessionService.getStudentSessions(jwt.getSubject()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SessionDTO>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @PutMapping("/{id}/payment")
    public ResponseEntity<SessionDTO> updatePayment(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        return ResponseEntity.ok(sessionService.updatePaymentStatus(id, body.get("status")));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<SessionDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        return ResponseEntity.ok(sessionService.updateSessionStatus(id, body.get("status")));
    }

    @PutMapping("/{id}/meeting-link")
    public ResponseEntity<SessionDTO> addMeetingLink(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        return ResponseEntity.ok(sessionService.addMeetingLink(id, body.get("meetingLink")));
    }

    @PostMapping("/{id}/payment-proof")
    public ResponseEntity<SessionDTO> uploadPaymentProof(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal Jwt jwt
    ) throws IOException {
        return ResponseEntity.ok(sessionService.uploadPaymentProof(id, file, jwt.getSubject()));
    }

    @PostMapping("/{id}/reschedule")
    public ResponseEntity<SessionDTO> requestReschedule(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(sessionService.requestReschedule(
                id, body.get("newDate"), body.get("newTime"), body.get("reason"), jwt.getSubject()));
    }

    @PutMapping("/{id}/reschedule/approve")
    public ResponseEntity<SessionDTO> approveReschedule(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.approveReschedule(id));
    }

    @PutMapping("/{id}/reschedule/reject")
    public ResponseEntity<SessionDTO> rejectReschedule(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.rejectReschedule(id));
    }
}
