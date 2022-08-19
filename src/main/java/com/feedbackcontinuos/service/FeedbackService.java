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

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final UsersService usersService;
    private final TagsService tagsService;
    private final FeedBackRepository feedbackRepository;
    private final ObjectMapper objectMapper;

    public void create(FeedbackCreateDTO createDTO) throws RegraDeNegocioException {
        UsersEntity userSend = usersService.getLoggedUser();
        UsersEntity userRecived = usersService.findById(createDTO.getFeedbackUserId());
        FeedBackEntity feedBack = objectMapper.convertValue(createDTO, FeedBackEntity.class);
        feedBack.setFeedbackEntityGiven(userSend);
        feedBack.setFeedbackEntityReceived(userRecived);
        feedBack.setDataEHora(LocalDateTime.now(ZoneId.systemDefault()));
        List<TagEntity> tags = new ArrayList<>();
        for (TagCreateDTO tag : createDTO.getTagsList()) {
             tags.add(tagsService.tagCreate(tag));
        }
        feedBack.setTagsList(tags);
        feedbackRepository.save(feedBack);
    }

    public PageDTO<FeedbackGivedDTO> getGivedFeedbacks(Integer page) throws RegraDeNegocioException {
        UsersEntity usersEntity = usersService.getLoggedUser();
        Pageable pageable = PageRequest.of(page, 3, Sort.Direction.DESC, "dataEHora");
        return getFeedbackGivedDTOPageDTO(page, usersEntity, pageable);
    }

    public PageDTO<FeedbackRecivedDTO> getReceivedFeedbacks(Integer page) throws RegraDeNegocioException {
        UsersEntity usersEntity = usersService.getLoggedUser();
        Pageable pageable = PageRequest.of(page, 3, Sort.Direction.DESC, "dataEHora");
        return getFeedbackRecivedDTOPageDTO(page, usersEntity, pageable);
    }

    public PageDTO<FeedbackGivedDTO> getGivedFeedbacksIdUser(Integer page, Integer id) throws RegraDeNegocioException {
        UsersEntity usersEntity = usersService.findById(id);
        Pageable pageable = PageRequest.of(page, 3, Sort.Direction.DESC, "dataEHora");
        return getFeedbackGivedDTOPageDTO(page, usersEntity, pageable);
    }

    public PageDTO<FeedbackRecivedDTO> getReceivedFeedbacksIdUser(Integer page, Integer id) throws RegraDeNegocioException {
        UsersEntity usersEntity = usersService.findById(id);
        Pageable pageable = PageRequest.of(page, 3, Sort.Direction.DESC, "dataEHora");
        return getFeedbackRecivedDTOPageDTO(page, usersEntity, pageable);
    }

    private PageDTO<FeedbackGivedDTO> getFeedbackGivedDTOPageDTO(Integer page, UsersEntity usersEntity, Pageable pageable) {
        Page<FeedBackEntity> pagina = feedbackRepository.findByUserId(pageable, usersEntity.getIdUser());
        List<FeedbackGivedDTO> feedbacks = pagina.getContent().stream()
                .map(feedBackEntity -> {
                    FeedbackGivedDTO feedbackDTO = objectMapper.convertValue(feedBackEntity, FeedbackGivedDTO.class);
                    feedbackDTO.setFeedbackEntityReceived(objectMapper.convertValue(feedBackEntity.getFeedbackEntityReceived(), UserWithNameAndAvatarDTO.class));
                    feedbackDTO.setTagsList(feedBackEntity.getTagsList().stream()
                            .map(tagEntity -> objectMapper.convertValue(tagEntity, TagCreateDTO.class))
                            .toList());
                    return feedbackDTO;
                })
                .toList();
        return new PageDTO<>(pagina.getTotalElements(), pagina.getTotalPages(), page, 3, feedbacks);
    }

    private PageDTO<FeedbackRecivedDTO> getFeedbackRecivedDTOPageDTO(Integer page, UsersEntity usersEntity, Pageable pageable) {
        Page<FeedBackEntity> pagina = feedbackRepository.findByFeedbackUserId(pageable, usersEntity.getIdUser());
        List<FeedbackRecivedDTO> feedbacks = pagina.getContent().stream()
                .map(feedBackEntity -> {
                    FeedbackRecivedDTO feedbackDTO = objectMapper.convertValue(feedBackEntity, FeedbackRecivedDTO.class);
                    feedbackDTO.setFeedbacksGiven(objectMapper.convertValue(feedBackEntity.getFeedbackEntityGiven(), UserWithNameAndAvatarDTO.class));
                    feedbackDTO.setTagsList(feedBackEntity.getTagsList().stream()
                            .map(tagEntity -> objectMapper.convertValue(tagEntity, TagCreateDTO.class))
                            .toList());
                    return feedbackDTO;
                })
                .toList();
        return new PageDTO<>(pagina.getTotalElements(), pagina.getTotalPages(), page, 3, feedbacks);
    }
}



