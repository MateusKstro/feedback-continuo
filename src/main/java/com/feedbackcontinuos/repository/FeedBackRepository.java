package com.feedbackcontinuos.repository;

import com.feedbackcontinuos.dto.FeedbackCompletoDTO;
import com.feedbackcontinuos.entity.FeedBackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackRepository extends JpaRepository <FeedBackEntity, Integer> {

    Page<FeedBackEntity> findByFeedbackUserId(Pageable pageable, Integer idUser);

    Page<FeedBackEntity> findByUserId(Pageable pageable, Integer idUser);
}
