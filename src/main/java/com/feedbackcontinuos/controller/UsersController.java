package com.feedbackcontinuos.controller;

import com.feedbackcontinuos.entitys.UsersEntity;
import com.feedbackcontinuos.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {

    private final UsersRepository usersRepository;

    @PostMapping
    public void post(UsersEntity usersEntity){
        usersRepository.save(usersEntity);
    }
}
