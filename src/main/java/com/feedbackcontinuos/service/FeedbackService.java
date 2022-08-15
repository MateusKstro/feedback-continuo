package com.feedbackcontinuos.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.FeedbackCreateDTO;
import com.feedbackcontinuos.dto.FeedbackDTO;
import com.feedbackcontinuos.entity.FeedBackEntity;
import com.feedbackcontinuos.entity.TagEntity;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import com.feedbackcontinuos.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final UsersService usersService;

    private final TagsService tagsService;

    private final ObjectMapper objectMapper;

    private final FeedbackRepository feedbackRepository;


    public void create(FeedbackCreateDTO createDTO) throws RegraDeNegocioException {

        UsersEntity usersEntity = usersService.getLoggedUser();
        if (createDTO.getFeedbackIdUser().equals(usersEntity.getIdUser())) {
            throw new RegraDeNegocioException("Nao é possivel fazer feedback para si mesmo");
        }
        criandoFeedback(createDTO);
    }


    private void criandoFeedback(FeedbackCreateDTO createDTO) throws RegraDeNegocioException {

        FeedBackEntity entity = toFeedbackEntity(createDTO);
        feedbackRepository.save(entity);
    }

    private FeedBackEntity toFeedbackEntity(FeedbackCreateDTO feedbackCreateDTO) throws RegraDeNegocioException {
        UsersEntity given = usersService.getLoggedUser();

        UsersEntity received = usersService.findById(Integer.valueOf(feedbackCreateDTO.getFeedbackIdUser()));

        Set<TagEntity> tags = new HashSet<>();
        feedbackCreateDTO.getTagsList().forEach(tag -> {
            TagEntity tagEntity = tagsService.findTag(tag);
            tags.add(tagEntity);
        });

         return FeedBackEntity.builder()
                .anonymous(feedbackCreateDTO.getAnonymous())
                .feedbackEntityGiven(given)
                .feedbackEntityReceived(received)
                .feedbackUserId(received.getIdUser())
                .userId(given.getIdUser())
                .dataEHora(LocalDateTime.now())
                .message(feedbackCreateDTO.getMessage())
                .tagEntities(tags)
                .build();
    }
}
