package com.skillmentor.repository;

import com.skillmentor.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    List<Session> findByStudentClerkIdOrderBySessionDateDesc(String studentClerkId);

    List<Session> findAllByOrderBySessionDateDesc();

    // Check for overlapping sessions for a student with the same mentor
    @Query("SELECT s FROM Session s WHERE s.studentClerkId = :studentId AND s.mentor.id = :mentorId " +
            "AND s.sessionStatus != 'CANCELLED' " +
            "AND s.sessionDate < :endTime AND :startTime < FUNCTION('TIMESTAMPADD', MINUTE, s.duration, s.sessionDate)")
    List<Session> findOverlappingSessions(
            @Param("studentId") String studentClerkId,
            @Param("mentorId") Long mentorId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    // Check for duplicate subject + time booking
    @Query("SELECT s FROM Session s WHERE s.studentClerkId = :studentId AND s.subject.id = :subjectId " +
            "AND s.sessionStatus != 'CANCELLED' " +
            "AND s.sessionDate < :endTime AND :startTime < FUNCTION('TIMESTAMPADD', MINUTE, s.duration, s.sessionDate)")
    List<Session> findDuplicateSubjectBookings(
            @Param("studentId") String studentClerkId,
            @Param("subjectId") Long subjectId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
