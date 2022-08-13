package com.feedbackcontinuos.repository;

import com.feedbackcontinuos.entity.FeedBackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository <FeedBackEntity, Integer> {
}
