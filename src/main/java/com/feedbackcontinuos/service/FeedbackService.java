package com.feedbackcontinuos.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.*;
import com.feedbackcontinuos.entity.FeedBackEntity;
import com.feedbackcontinuos.entity.TagEntity;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import com.feedbackcontinuos.repository.FeedBackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final UsersService usersService;
    private final TagsService tagsService;
    private final FeedBackRepository feedbackRepository;
    private final ObjectMapper objectMapper;

    //test
    public void create(FeedbackCreateDTO createDTO) throws RegraDeNegocioException {
        UsersEntity userSend = usersService.getLoggedUser();
        UsersEntity userRecived = usersService.findById(createDTO.getFeedbackUserId());
        FeedBackEntity feedBack = objectMapper.convertValue(createDTO, FeedBackEntity.class);
        feedBack.setFeedbackEntityGiven(userSend);
        feedBack.setFeedbackEntityReceived(userRecived);
        feedBack.setDataEHora(LocalDateTime.now(ZoneId.systemDefault()));
        for (TagCreateDTO tag : createDTO.getTagsList()) {
            if (tagsService.existsByName(tag.getName())) {
                break;
            } else {
                tagsService.tagCreate(tag);
            }
        }
        feedbackRepository.save(feedBack);
    }


    public PageDTO<FeedbackDTO> getReceivedFeedbacks(Integer page) throws RegraDeNegocioException {
        UsersEntity usersEntity = usersService.getLoggedUser();

        Pageable pageable = PageRequest.of(page, 3, Sort.Direction.DESC, "dataEHora");

        Page<FeedBackEntity> pagina = feedbackRepository.findByFeedbackUserId(pageable, usersEntity.getIdUser());

        List<FeedbackDTO> feedbacks = pagina.getContent().stream()
                .map(feedBackEntity -> objectMapper.convertValue(feedBackEntity, FeedbackDTO.class))
                .toList();
        return new PageDTO<>(pagina.getTotalElements(), pagina.getTotalPages(), page, 3, feedbacks);
    }

    public PageDTO<FeedbackDTO> getGivedFeedbacks(Integer page) throws RegraDeNegocioException {
        UsersEntity usersEntity = usersService.getLoggedUser();

        Pageable pageable = PageRequest.of(page, 3, Sort.Direction.DESC, "dataEHora");

        Page<FeedBackEntity> pagina = feedbackRepository.findByUserId(pageable, usersEntity.getIdUser());

        List<FeedbackDTO> feedbacks = pagina.getContent().stream()
                .map(feedBackEntity -> objectMapper.convertValue(feedBackEntity, FeedbackDTO.class))
                .toList();
        return new PageDTO<>(pagina.getTotalElements(), pagina.getTotalPages(), page, 3, feedbacks);
    }



}

