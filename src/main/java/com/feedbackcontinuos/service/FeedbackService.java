package com.feedbackcontinuos.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.FeedbackCompletoDTO;
import com.feedbackcontinuos.dto.FeedbackCreateDTO;
import com.feedbackcontinuos.dto.FeedbackDTO;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final UsersService usersService;

    private final TagsService tagsService;

    private final FeedbackRepository feedbackRepository;


    public void create(FeedbackCreateDTO createDTO) throws RegraDeNegocioException {

        UsersEntity usersEntity = usersService.getLoggedUser();
        if (createDTO.getFeedbackIdUser().equals(usersEntity.getIdUser())) {
            throw new RegraDeNegocioException("Nao é possivel fazer feedback para si mesmo");
        }
        criandoFeedback(createDTO);
    }


    public Page<FeedbackCompletoDTO> getReceivedFeedbacks(Integer page) throws RegraDeNegocioException {
        UsersEntity usersEntity = usersService.getLoggedUser();

        Pageable pageable = PageRequest.of(page,3, Sort.Direction.DESC, "dataEHora");

        return feedbackRepository.findByFeedbackUserId(pageable, usersEntity.getIdUser())
                .map(feedBackEntity -> {
                    try{
                        UsersEntity receveid = usersService.findById(feedBackEntity.getUserId());

                        String avatar;
                        if(receveid.getAvatar() == null){
                            avatar = null;
                        } else {
                            avatar = Base64.getEncoder().encodeToString(receveid.getAvatar());
                        }

                        return FeedbackCompletoDTO.builder()
                                .feedbackId(feedBackEntity.getIdFeedback())
                                .userName(feedBackEntity.getAnonymous() ? "Anonymous" : receveid.getUsername())
                                .avatar(feedBackEntity.getAnonymous() ? null : avatar)
                                .message(feedBackEntity.getMessage())
                                .tags(getTags(feedBackEntity.getTagEntities()))
                                .dataEHora(feedBackEntity.getDataEHora())
                                .build();
                    } catch (RegraDeNegocioException e){
                        throw new RuntimeException(e);
                    }
                });
    }

    public Page<FeedbackCompletoDTO> getGivedFeedbacks(Integer page) throws RegraDeNegocioException {
        UsersEntity user = usersService.getLoggedUser();

        Pageable pageable = PageRequest.of(page, 3, Sort.Direction.DESC, "dataEHora");

        return feedbackRepository.findByFeedbackUserId(pageable, user.getIdUser())
                .map(feedBackEntity -> {
                    try{
                        UsersEntity gived = usersService.findById(feedBackEntity.getFeedbackUserId());

                        return FeedbackCompletoDTO.builder()
                                .feedbackId(feedBackEntity.getIdFeedback())
                                .userName(gived.getUsername())
                                .avatar(gived.getAvatar() == null ? null :Base64.getEncoder().encodeToString(gived.getAvatar()))
                                .message(feedBackEntity.getMessage())
                                .tags(getTags(feedBackEntity.getTagEntities()))
                                .dataEHora(feedBackEntity.getDataEHora())
                                .build();
                    } catch (RegraDeNegocioException e){
                        throw new RuntimeException(e);
                    }
                });
    }


    private List<String> getTags(Set<TagEntity> tags){
        List<String> tagsList = new ArrayList<>();
        tags.forEach(tagEntity -> {
            String tag = tagEntity.getName().toUpperCase().replace(" ", "_");
            tagsList.add(tag);
        });
        return tagsList;
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
