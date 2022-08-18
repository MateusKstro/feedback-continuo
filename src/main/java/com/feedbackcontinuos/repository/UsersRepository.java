package com.feedbackcontinuos.repository;

import com.feedbackcontinuos.entity.UsersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {

    @Query("select u" +
            " from users u " +
            " where u.idUser <> :idUser" +
            " order by u.name asc ")
    Page<UsersEntity> page(@Param("idUser") Integer idUser, Pageable pageable);
    Optional<UsersEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
