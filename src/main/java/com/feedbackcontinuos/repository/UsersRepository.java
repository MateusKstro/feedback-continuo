package com.feedbackcontinuos.repository;

import com.feedbackcontinuos.entitys.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
}
