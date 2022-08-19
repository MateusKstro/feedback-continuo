package com.feedbackcontinuos.repository;

import com.feedbackcontinuos.entity.FeedBackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedBackRepository extends JpaRepository <FeedBackEntity, Integer> {

    @Query(" select f " +
            "  from feedback f" +
            " where f.userId = :idUser " +
            " order by f.dataEHora desc ")
    Page<FeedBackEntity> findByUserIdGived(@Param("idUser")Integer idUser, Pageable pageable);

    @Query(" select f " +
            "  from feedback f" +
            " where f.feedbackUserId = :idUser " +
            " order by f.idFeedback desc ")
    Page<FeedBackEntity> findByUserIdRecived(@Param("idUser")Integer idUser, Pageable pageable);
}
