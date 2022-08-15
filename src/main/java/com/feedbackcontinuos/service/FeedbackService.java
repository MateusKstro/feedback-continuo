package com.feedbackcontinuos.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.FeedbackCreateDTO;
import com.feedbackcontinuos.dto.FeedbackDTO;
import com.feedbackcontinuos.entity.FeedBackEntity;
import com.feedbackcontinuos.entity.TagEntity;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final UsersService usersService;

    private final TagsService tagsService;

    private final ObjectMapper objectMapper;


    public FeedbackDTO create(FeedbackCreateDTO createDTO) throws RegraDeNegocioException {
        if (createDTO.getAnonymous()) {
            criandoFeedback(createDTO);
        } else {
            UsersEntity usersEntity = usersService.getLoggedUser();
            if (createDTO.getFeedbackIdUser().equals(usersEntity.getIdUser())) {
                throw new RegraDeNegocioException("Nao é possivel fazer feedback para si mesmo");
            }
            criandoFeedback(createDTO);
        }
        return objectMapper.convertValue(createDTO, FeedbackDTO.class);
    }






    private void criandoFeedback(FeedbackCreateDTO createDTO) throws RegraDeNegocioException {
        UsersEntity received = usersService.findById(Integer.valueOf(createDTO.getFeedbackIdUser()));

        Set<TagEntity> tags = new HashSet<>();
        createDTO.getTagsList().forEach(tag -> {
            TagEntity tagEntity = tagsService.findTag(tag);
            tags.add(tagEntity);
        });

        FeedBackEntity feedBackEntity = objectMapper.convertValue(createDTO, FeedBackEntity.class);
    }
}
