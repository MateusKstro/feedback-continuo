package com.feedbackcontinuos.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.*;
import com.feedbackcontinuos.entity.FeedBackEntity;
import com.feedbackcontinuos.entity.TagEntity;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import com.feedbackcontinuos.repository.FeedBackRepository;
import com.feedbackcontinuos.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final UsersService usersService;
    private final TagsService tagsService;
    private final FeedBackRepository feedbackRepository;

    private final UsersRepository usersRepository;
    private final ObjectMapper objectMapper;

    public void create(FeedbackCreateDTO createDTO) throws RegraDeNegocioException {
        UsersEntity userSend = usersService.getLoggedUser();
        if(createDTO.getFeedbackUserId().equals(userSend.getIdUser())) {
            throw new RegraDeNegocioException("Não é possivel dar feedback para si mesmo");
        } else {
            UsersEntity userRecived = usersService.findById(createDTO.getFeedbackUserId());
            FeedBackEntity feedBack = objectMapper.convertValue(createDTO, FeedBackEntity.class);
            feedBack.setFeedbackEntityGiven(userSend);
            feedBack.setPublico(true);
            feedBack.setFeedbackEntityReceived(userRecived);
            feedBack.setDataEHora(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")));
            if (createDTO.getTagsList() != null) {
                List<TagEntity> tags = new ArrayList<>();
                for (TagCreateDTO tag : createDTO.getTagsList()) {
                    tags.add(tagsService.tagCreate(tag));
                }
                feedBack.setTagsList(tags);
            }
            feedbackRepository.save(feedBack);
        }
    }

    public void updateFeedback(Integer id, boolean publico) {
        Optional<FeedBackEntity> feedBack = feedbackRepository.findById(id);
        if (feedBack.isPresent()) {
            feedBack.get().setPublico(publico);
            feedbackRepository.save(feedBack.get());
        }
    }

    public PageDTO<FeedbackGivedDTO> getGivedFeedbacks(Integer page) throws RegraDeNegocioException {
        Pageable pageable = PageRequest.of(page, 3);
        Page<FeedBackEntity> pagina = feedbackRepository.findByUserIdGived(usersService.getLoggedUser().getIdUser(), pageable);
        return getFeedbackGivedDTOPageDTO(page, pagina);
    }

    public PageDTO<FeedbackRecivedDTO> getReceivedFeedbacks(Integer page) throws RegraDeNegocioException {
        Pageable pageable = PageRequest.of(page, 3);
        Page<FeedBackEntity> pagina = feedbackRepository.findByUserIdRecived(usersService.getLoggedUser().getIdUser(), pageable);
        return getFeedbackRecivedDTOPageDTO(page, pagina);
    }

    public PageDTO<FeedbackGivedDTO> getGivedFeedbacksIdUser(Integer page, Integer id) {
        Pageable pageable = PageRequest.of(page, 3);
        Page<FeedBackEntity> pagina = feedbackRepository.findByUserIdGived(id, pageable);
        return getFeedbackGivedDTOPageDTO(page, pagina);
    }

    public PageDTO<FeedbackRecivedDTO> getReceivedFeedbacksIdUser(Integer page, Integer id) {
        Pageable pageable = PageRequest.of(page, 3);
        Page<FeedBackEntity> pagina = feedbackRepository.findByUserIdRecived(id, pageable);
        return getFeedbackRecivedDTOPageDTO(page, pagina);
    }

    private PageDTO<FeedbackGivedDTO> getFeedbackGivedDTOPageDTO(Integer page, Page<FeedBackEntity> pagina) {
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

    private PageDTO<FeedbackRecivedDTO> getFeedbackRecivedDTOPageDTO(Integer page, Page<FeedBackEntity> pagina) {
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



