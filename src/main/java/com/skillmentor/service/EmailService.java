package com.skillmentor.service;

import com.skillmentor.entity.Session;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:noreply@skillmentor.com}")
    private String fromEmail;

    @Value("${app.mail.enabled:false}")
    private boolean emailEnabled;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("h:mm a");

    @Async
    public void sendBookingConfirmationToStudent(Session session) {
        if (!emailEnabled) {
            log.info("Email disabled — skipping booking confirmation to student: {}", session.getStudentEmail());
            return;
        }

        String mentorName = session.getMentor().getFirstName() + " " + session.getMentor().getLastName();
        String subject = "Booking Confirmed — " + session.getSubject().getName();
        String html = buildEmail(
                "Session Booked! 🎉",
                "Hi " + session.getStudentName() + ",",
                "<p>Your mentoring session has been successfully booked. Here are the details:</p>"
                        + detailsBlock(session, mentorName)
                        + "<p>Please upload your payment proof on the <strong>Payment</strong> page to confirm your booking.</p>",
                "Go to Dashboard",
                "/dashboard"
        );

        sendHtml(session.getStudentEmail(), subject, html);
    }

    @Async
    public void sendBookingNotificationToMentor(Session session, String mentorEmail) {
        if (!emailEnabled || mentorEmail == null) {
            log.info("Email disabled or no mentor email — skipping booking notification");
            return;
        }

        String mentorName = session.getMentor().getFirstName();
        String subject = "New Booking — " + session.getSubject().getName();
        String html = buildEmail(
                "New Session Booked 📅",
                "Hi " + mentorName + ",",
                "<p>A student has booked a session with you:</p>"
                        + detailsBlock(session, session.getStudentName())
                        + "<p>You'll be notified once payment is confirmed.</p>",
                null, null
        );

        sendHtml(mentorEmail, subject, html);
    }

    @Async
    public void sendPaymentConfirmedToStudent(Session session) {
        if (!emailEnabled) {
            log.info("Email disabled — skipping payment confirmation to student: {}", session.getStudentEmail());
            return;
        }

        String mentorName = session.getMentor().getFirstName() + " " + session.getMentor().getLastName();
        String subject = "Payment Confirmed ✅ — " + session.getSubject().getName();
        String html = buildEmail(
                "Payment Confirmed! ✅",
                "Hi " + session.getStudentName() + ",",
                "<p>Your payment has been confirmed and your session is now scheduled:</p>"
                        + detailsBlock(session, mentorName)
                        + (session.getMeetingLink() != null
                        ? "<p><strong>Meeting Link:</strong> <a href=\"" + session.getMeetingLink() + "\">" + session.getMeetingLink() + "</a></p>"
                        : "<p>Your mentor will share the meeting link soon.</p>"),
                "View Dashboard",
                "/dashboard"
        );

        sendHtml(session.getStudentEmail(), subject, html);
    }

    @Async
    public void sendSessionCompletedToStudent(Session session) {
        if (!emailEnabled) {
            log.info("Email disabled — skipping session completed to student: {}", session.getStudentEmail());
            return;
        }

        String mentorName = session.getMentor().getFirstName() + " " + session.getMentor().getLastName();
        String subject = "Session Completed — " + session.getSubject().getName();
        String html = buildEmail(
                "Session Completed! 🎓",
                "Hi " + session.getStudentName() + ",",
                "<p>Your mentoring session has been marked as completed:</p>"
                        + detailsBlock(session, mentorName)
                        + "<p>We'd love to hear your feedback! Please leave a review for your mentor.</p>",
                "Leave a Review",
                "/mentors/" + session.getMentor().getId()
        );

        sendHtml(session.getStudentEmail(), subject, html);
    }

    @Async
    public void sendSessionCancelledNotification(Session session) {
        if (!emailEnabled) {
            log.info("Email disabled — skipping cancellation notification");
            return;
        }

        String mentorName = session.getMentor().getFirstName() + " " + session.getMentor().getLastName();
        String subject = "Session Cancelled — " + session.getSubject().getName();
        String html = buildEmail(
                "Session Cancelled",
                "Hi " + session.getStudentName() + ",",
                "<p>The following session has been cancelled:</p>"
                        + detailsBlock(session, mentorName)
                        + "<p>If you have any questions, please reach out to us.</p>",
                null, null
        );

        sendHtml(session.getStudentEmail(), subject, html);
    }

    // ============ Helpers ============

    private String detailsBlock(Session session, String otherPartyName) {
        return "<table style=\"width:100%;border-collapse:collapse;margin:16px 0;\">"
                + row("📚 Subject", session.getSubject().getName())
                + row("👤 With", otherPartyName)
                + row("📅 Date", session.getSessionDate().toLocalDate().format(DATE_FMT))
                + row("🕐 Time", session.getSessionDate().toLocalTime().format(TIME_FMT))
                + row("⏱ Duration", session.getDuration() + " minutes")
                + "</table>";
    }

    private String row(String label, String value) {
        return "<tr>"
                + "<td style=\"padding:8px 12px;border-bottom:1px solid #f0f0f0;color:#888;font-size:14px;\">" + label + "</td>"
                + "<td style=\"padding:8px 12px;border-bottom:1px solid #f0f0f0;font-weight:600;font-size:14px;\">" + value + "</td>"
                + "</tr>";
    }

    private String buildEmail(String heading, String greeting, String bodyContent, String ctaText, String ctaPath) {
        String ctaHtml = "";
        if (ctaText != null && ctaPath != null) {
            ctaHtml = "<div style=\"text-align:center;margin:24px 0;\">"
                    + "<a href=\"" + ctaPath + "\" style=\"display:inline-block;background:#6366f1;color:#fff;"
                    + "padding:12px 32px;border-radius:8px;text-decoration:none;font-weight:600;font-size:14px;\">"
                    + ctaText + "</a></div>";
        }

        return "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"></head>"
                + "<body style=\"margin:0;padding:0;background:#f8fafc;font-family:'Segoe UI',Arial,sans-serif;\">"
                + "<div style=\"max-width:560px;margin:40px auto;background:#ffffff;border-radius:16px;"
                + "overflow:hidden;box-shadow:0 4px 24px rgba(0,0,0,0.06);\">"
                // Header
                + "<div style=\"background:linear-gradient(135deg,#6366f1,#8b5cf6);padding:32px;text-align:center;\">"
                + "<h1 style=\"color:#fff;margin:0;font-size:22px;font-weight:700;\">" + heading + "</h1>"
                + "</div>"
                // Body
                + "<div style=\"padding:32px;\">"
                + "<p style=\"font-size:16px;color:#1e293b;margin:0 0 16px;\">" + greeting + "</p>"
                + "<div style=\"font-size:14px;color:#334155;line-height:1.7;\">" + bodyContent + "</div>"
                + ctaHtml
                + "</div>"
                // Footer
                + "<div style=\"padding:20px 32px;background:#f8fafc;text-align:center;border-top:1px solid #f0f0f0;\">"
                + "<p style=\"margin:0;font-size:12px;color:#94a3b8;\">SkillMentor — Your online mentoring platform</p>"
                + "</div>"
                + "</div></body></html>";
    }

    private void sendHtml(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
            log.info("Email sent to {} — {}", to, subject);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
