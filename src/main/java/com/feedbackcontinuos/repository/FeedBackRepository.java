package com.feedbackcontinuos.repository;

import com.feedbackcontinuos.entity.FeedBackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedBackRepository extends JpaRepository <FeedBackEntity, Integer> {
    @Query(" select f " +
            "  from feedback f" +
            " where f.userId = :idUser " +
            " order by f.dataEHora desc ")
    List<FeedBackEntity> findByUserIdGived(@Param("idUser")Integer idUser);

    @Query(" select f " +
            "  from feedback f" +
            " where f.feedbackUserId = :idUser " +
            " order by f.idFeedback desc ")
    List<FeedBackEntity> findByUserIdRecived(@Param("idUser")Integer idUser);
}
