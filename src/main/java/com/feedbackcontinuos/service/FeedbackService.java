package com.feedbackcontinuos.service;


import com.feedbackcontinuos.dto.FeedbackCreateDTO;
import com.feedbackcontinuos.dto.FeedbackDTO;
import com.feedbackcontinuos.entity.UsersEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final UsersService usersService;


    public FeedbackDTO create(FeedbackCreateDTO createDTO){

}
