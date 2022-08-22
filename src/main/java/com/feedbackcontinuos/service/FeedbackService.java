package com.feedbackcontinuos.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.*;
import com.feedbackcontinuos.entity.FeedBackEntity;
import com.feedbackcontinuos.entity.TagEntity;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import com.feedbackcontinuos.repository.FeedBackRepository;
import lombok.RequiredArgsConstructor;
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
    private final ObjectMapper objectMapper;

    public void create(FeedbackCreateDTO createDTO) throws RegraDeNegocioException {
        UsersEntity userSend = usersService.getLoggedUser();
        if (!createDTO.getFeedbackUserId().equals(userSend.getIdUser())) {
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
        } else {
            throw new RegraDeNegocioException("Não é possivel dar feedback para si mesmo");
        }
    }

    public void updateFeedback(Integer id, boolean publico) throws RegraDeNegocioException {
        Optional<FeedBackEntity> feedBack = feedbackRepository.findById(id);
        if (feedBack.isPresent()) {
            feedBack.get().setPublico(publico);
            feedbackRepository.save(feedBack.get());
        } else {
            throw new RegraDeNegocioException("Feedback inexistente");
        }
    }

    public List<FeedbackGivedDTO> getGivedFeedbacks() throws RegraDeNegocioException {
        return getFeedbackGivedDTOS(usersService.getLoggedUser().getIdUser());
    }
    public List<FeedbackRecivedDTO> getReceivedFeedbacks() throws RegraDeNegocioException {
        return getFeedbackRecivedDTOS(usersService.getLoggedUser().getIdUser());
    }
    public List<FeedbackGivedDTO> getGivedFeedbacksIdUser(Integer id) {
        return getFeedbackGivedDTOS(id);
    }

    public List<FeedbackRecivedDTO> getReceivedFeedbacksIdUser(Integer id) {
        return getFeedbackRecivedDTOS(id);
    }

    private List<FeedbackGivedDTO> getFeedbackGivedDTOS(Integer id) {
        return feedbackRepository
                .findByUserIdGived(id).stream()
                .map(feedBackEntity -> {
                    FeedbackGivedDTO feedbackDTO = objectMapper.convertValue(feedBackEntity, FeedbackGivedDTO.class);
                    feedbackDTO.setFeedbackEntityReceived(objectMapper.convertValue(feedBackEntity.getFeedbackEntityReceived(), UserWithNameAndAvatarDTO.class));
                    feedbackDTO.setTagsList(feedBackEntity.getTagsList().stream()
                            .map(tagEntity -> objectMapper.convertValue(tagEntity, TagCreateDTO.class))
                            .toList());
                    return feedbackDTO;
                })
                .toList();
    }

    private List<FeedbackRecivedDTO> getFeedbackRecivedDTOS(Integer id) {
        return feedbackRepository
                .findByUserIdRecived(id).stream()
                .map(feedBackEntity -> {
                    FeedbackRecivedDTO feedbackDTO = objectMapper.convertValue(feedBackEntity, FeedbackRecivedDTO.class);
                    feedbackDTO.setFeedbacksGiven(objectMapper.convertValue(feedBackEntity.getFeedbackEntityGiven(), UserWithNameAndAvatarDTO.class));
                    feedbackDTO.setTagsList(feedBackEntity.getTagsList().stream()
                            .map(tagEntity -> objectMapper.convertValue(tagEntity, TagCreateDTO.class))
                            .toList());
                    return feedbackDTO;
                })
                .toList();
    }
}



