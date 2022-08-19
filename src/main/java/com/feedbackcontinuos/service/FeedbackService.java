package com.feedbackcontinuos.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.FeedbackCreateDTO;
import com.feedbackcontinuos.dto.FeedbackDTO;
import com.feedbackcontinuos.dto.PageDTO;
import com.feedbackcontinuos.dto.TagCreateDTO;
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

    public PageDTO<FeedbackDTO> getGivedFeedbacks(Integer page) throws RegraDeNegocioException {
        UsersEntity usersEntity = usersService.getLoggedUser();
        Pageable pageable = PageRequest.of(page, 3, Sort.Direction.DESC, "dataEHora");
        Page<FeedBackEntity> pagina = feedbackRepository.findByUserId(pageable, usersEntity.getIdUser());
        return getFeedbackDTOPageDTO(page, pagina);
    }

    public PageDTO<FeedbackDTO> getReceivedFeedbacks(Integer page) throws RegraDeNegocioException {
        UsersEntity usersEntity = usersService.getLoggedUser();
        Pageable pageable = PageRequest.of(page, 3, Sort.Direction.DESC, "dataEHora");
        Page<FeedBackEntity> pagina = feedbackRepository.findByFeedbackUserId(pageable, usersEntity.getIdUser());
        return getFeedbackDTOPageDTO(page, pagina);
    }

    public PageDTO<FeedbackDTO> getReceivedFeedbacksIdUser(Integer page, Integer id) throws RegraDeNegocioException {
        UsersEntity usersEntity = usersService.findById(id);
        Pageable pageable = PageRequest.of(page, 3, Sort.Direction.DESC, "dataEHora");
        Page<FeedBackEntity> pagina = feedbackRepository.findByFeedbackUserId(pageable, usersEntity.getIdUser());
        return getFeedbackDTOPageDTO(page, pagina);
    }

    public PageDTO<FeedbackDTO> getGivedFeedbacksIdUser(Integer page, Integer id) throws RegraDeNegocioException {
        UsersEntity usersEntity = usersService.findById(id);
        Pageable pageable = PageRequest.of(page, 3, Sort.Direction.DESC, "dataEHora");
        Page<FeedBackEntity> pagina = feedbackRepository.findByUserId(pageable, usersEntity.getIdUser());
        return getFeedbackDTOPageDTO(page, pagina);
    }

    private PageDTO<FeedbackDTO> getFeedbackDTOPageDTO(Integer page, Page<FeedBackEntity> pagina) {
        List<FeedbackDTO> feedbacks = pagina.getContent().stream()
                .map(feedBackEntity -> {
                    FeedbackDTO feedbackDTO = objectMapper.convertValue(feedBackEntity, FeedbackDTO.class);
                    feedbackDTO.setTagsList(feedBackEntity.getTagsList().stream()
                            .map(tagEntity -> objectMapper.convertValue(tagEntity, TagCreateDTO.class))
                            .toList());
                    return feedbackDTO;
                })
                .toList();
        return new PageDTO<>(pagina.getTotalElements(), pagina.getTotalPages(), page, 3, feedbacks);
    }
}



