package com.skillmentor.repository;

import com.skillmentor.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMentorIdOrderByCreatedAtDesc(Long mentorId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.mentor.id = :mentorId")
    Double findAverageRatingByMentorId(@Param("mentorId") Long mentorId);

    long countByMentorId(Long mentorId);
}
