package com.feedbackcontinuos.service;


import com.feedbackcontinuos.dto.FeedbackCreateDTO;
import com.feedbackcontinuos.dto.FeedbackDTO;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final UsersService usersService;


//    public FeedbackDTO create(FeedbackCreateDTO createDTO) throws RegraDeNegocioException {
//        UsersEntity usersEntity = usersService.getLogedUserEntity();
//
//        if(createDTO.getFeedbackIdUser().equals(usersEntity.getIdUser())){
//            throw new RegraDeNegocioException("Nao e possivel fazer feedback para si mesmo");
//        }
//
//        UsersEntity received = usersService.getUserById(Integer.valueOf(createDTO.getFeedbackIdUser()));
//    }
}
