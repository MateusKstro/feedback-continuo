package com.feedbackcontinuos.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.FeedbackCompletoDTO;
import com.feedbackcontinuos.dto.FeedbackCreateDTO;
import com.feedbackcontinuos.dto.PageDTO;
import com.feedbackcontinuos.dto.TagDTO;
import com.feedbackcontinuos.entity.FeedBackEntity;
import com.feedbackcontinuos.entity.TagEntity;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import com.feedbackcontinuos.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final UsersService usersService;

    private final TagsService tagsService;

    private final FeedbackRepository feedbackRepository;

    private final ObjectMapper objectMapper;


    public void create(FeedbackCreateDTO createDTO) throws RegraDeNegocioException {
        UsersEntity userSend = usersService.getLoggedUser();
        UsersEntity userRecived = usersService.findById(createDTO.getFeedbackIdUser());
        FeedBackEntity feedBack = objectMapper.convertValue(createDTO, FeedBackEntity.class);
        feedBack.setFeedbackEntityGiven(userSend);
        feedBack.setFeedbackEntityReceived(userRecived);
        feedBack.setDataEHora(LocalDateTime.now(ZoneId.systemDefault()));
        for (TagDTO tag : createDTO.getTagsList()) {
            if (tagsService.existsByName(tag.getName())) {
                break;
            } else {
                tagsService.tagCreate(tag);
            }
        }
        feedbackRepository.save(feedBack);
    }


    public PageDTO<FeedbackCompletoDTO> getReceivedFeedbacks(Integer page) throws RegraDeNegocioException {
        List<FeedBackEntity> usersEntity = usersService.getLoggedUser().getFeedBackReceived();

        Pageable pageable = PageRequest.of(page, 3, Sort.Direction.DESC, "dataEHora");

        Page<FeedBackEntity> pagina = feedbackRepository.findByFeedBackUserId(pageable);

        List<FeedbackCompletoDTO> feedbacks = pagina.getContent().stream()
                .map(feedBackEntity -> objectMapper.convertValue(feedBackEntity, FeedbackCompletoDTO.class))
                .toList();
        return new PageDTO<>(pagina.getTotalElements(), pagina.getTotalPages(), page, 3, feedbacks);
    }


    public PageDTO<FeedbackCompletoDTO> getGivedFeedbacks(Integer page) throws RegraDeNegocioException {
        List<FeedBackEntity> user = usersService.getLoggedUser().getFeedbacksGiven();

        Pageable pageable = PageRequest.of(page, 3, Sort.Direction.DESC, "dataEHora");

        Page<FeedBackEntity> pagina = feedbackRepository.findByUserId(pageable);

        List<FeedbackCompletoDTO> feedbacks = pagina.getContent().stream()
                .map(feedBackEntity -> objectMapper.convertValue(feedBackEntity, FeedbackCompletoDTO.class))
                .toList();

        return new PageDTO<>(pagina.getTotalElements(), pagina.getTotalPages(), page, 3, feedbacks);
    }
}



