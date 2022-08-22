package com.feedbackcontinuos;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.feedbackcontinuos.dto.FeedbackCreateDTO;
import com.feedbackcontinuos.dto.TagCreateDTO;
import com.feedbackcontinuos.entity.AccessEntity;
import com.feedbackcontinuos.entity.FeedBackEntity;
import com.feedbackcontinuos.entity.TagEntity;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import com.feedbackcontinuos.repository.FeedBackRepository;
import com.feedbackcontinuos.service.FeedbackService;
import com.feedbackcontinuos.service.TagsService;
import com.feedbackcontinuos.service.UsersService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.Assert.*;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackServiceTest {

    @InjectMocks
    private FeedbackService feedbackService;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private FeedBackRepository feedBackRepository;
    @Mock
    private UsersService usersService;

    @Mock
    private TagsService tagsService;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(feedbackService, "objectMapper", objectMapper);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarCreateFeedbackByLoggedUserParaSiMesmo() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();
        FeedbackCreateDTO feedbackCreateDTO = getFeedbackCreatDTOParaSiMemso();
        when(usersService.getLoggedUser()).thenReturn(usersEntity);
        feedbackService.create(feedbackCreateDTO);
        assertNotNull(feedbackCreateDTO);
        assertSame(feedbackCreateDTO.getFeedbackUserId(), usersEntity.getIdUser());
        assertFalse(feedbackCreateDTO.getTagsList().isEmpty());
    }

    @Test
    public void deveTestarCreateFeedbackByLoggedUserComSucesso() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();
        FeedbackCreateDTO feedbackCreateDTO = getFeedbackCreatDTO();
        when(usersService.getLoggedUser()).thenReturn(usersEntity);
        feedbackService.create(feedbackCreateDTO);
        assertNotNull(feedbackCreateDTO);
        assertSame(feedbackCreateDTO.getFeedbackUserId(), 2);
        assertFalse(feedbackCreateDTO.getTagsList().isEmpty());
    }

    @Test
    public void deveTetarListDeFeedbacksReceived() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();
        List<FeedBackEntity> feedBackEntityList = new ArrayList<>();
        feedBackEntityList.add(FeedBackEntity.builder().tagsList(List.of(TagEntity.builder().build())).build());
        when(usersService.getLoggedUser()).thenReturn(usersEntity);
        when(feedBackRepository.findByUserIdRecived(anyInt())).thenReturn(feedBackEntityList);
        feedbackService.getReceivedFeedbacks();
        assertNotNull(feedBackEntityList);
    }

    @Test
    public void deveTetarListDeFeedbacksGived() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();
        List<FeedBackEntity> feedBackEntityList = new ArrayList<>();
        feedBackEntityList.add(FeedBackEntity.builder().tagsList(List.of(TagEntity.builder().build())).build());
        when(usersService.getLoggedUser()).thenReturn(usersEntity);
        when(feedBackRepository.findByUserIdGived(anyInt())).thenReturn(feedBackEntityList);
        feedbackService.getGivedFeedbacks();
        assertNotNull(feedBackEntityList);
    }

    @Test
    public void deveTetarListDeFeedbacksGivedIdUser() {
        Integer idFind = 1;
        List<FeedBackEntity> feedBackEntityList = new ArrayList<>();
        feedBackEntityList.add(FeedBackEntity.builder().tagsList(List.of(TagEntity.builder().build())).build());
        when(feedBackRepository.findByUserIdGived(anyInt())).thenReturn(feedBackEntityList);
        feedbackService.getGivedFeedbacksIdUser(idFind);
        assertNotNull(feedBackEntityList);
    }

    @Test
    public void deveTestarPublicoFeedBack() throws RegraDeNegocioException {
        Optional<FeedBackEntity> feedBackEntity = Optional.of(getFeedBackEntity());

        when(feedBackRepository.findById(anyInt())).thenReturn(feedBackEntity);

        feedbackService.updateFeedback(2, true);

        assertNotNull(feedBackEntity);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarPublicoFeedbackNull() throws RegraDeNegocioException {
        FeedBackEntity feedBackEntity = null;

        feedbackService.updateFeedback(2, true);

        assertNotNull(feedBackEntity);
    }

    private FeedbackCreateDTO getFeedbackCreatDTOParaSiMemso() {
        FeedbackCreateDTO feedbackCreateDTO = new FeedbackCreateDTO();
        feedbackCreateDTO.setMessage("teste");
        feedbackCreateDTO.setFeedbackUserId(1);
        feedbackCreateDTO.setAnonymous(false);
        feedbackCreateDTO.setTagsList(List.of(getTagCreateDTO()));
        return feedbackCreateDTO;
    }

    private FeedbackCreateDTO getFeedbackCreatDTO() {
        FeedbackCreateDTO feedbackCreateDTO = new FeedbackCreateDTO();
        feedbackCreateDTO.setMessage("teste");
        feedbackCreateDTO.setFeedbackUserId(2);
        feedbackCreateDTO.setAnonymous(false);
        feedbackCreateDTO.setTagsList(List.of(getTagCreateDTO()));
        return feedbackCreateDTO;
    }

    private TagCreateDTO getTagCreateDTO() {
        TagCreateDTO tagCreateDTO = new TagCreateDTO();
        tagCreateDTO.setName("JAVA");
        tagCreateDTO.setName("JAVASCRIPT");
        return tagCreateDTO;
    }

    private FeedBackEntity getFeedBackEntity() {
        FeedBackEntity feedBackEntity = new FeedBackEntity();
        feedBackEntity.setIdFeedback(1);
        feedBackEntity.setMessage("teste");
        feedBackEntity.setDataEHora(LocalDateTime.now());
        feedBackEntity.setAnonymous(false);
        feedBackEntity.setPublico(true);
        feedBackEntity.setUserId(1);
        feedBackEntity.setFeedbackUserId(2);
        return feedBackEntity;
    }

    private UsersEntity getUsersEntity() {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setIdUser(1);
        usersEntity.setAccessEntity(getAccessEntity());
        usersEntity.setName("Mateus de Castro");
        usersEntity.setUserRole("Desenvolvedor de Software");
        usersEntity.setEmail("mateus.castro@dbccompany.com.br");
        usersEntity.setUserPassword("mateus@123");
        usersEntity.setAvatar(null);
        return usersEntity;
    }

    private AccessEntity getAccessEntity() {
        AccessEntity accessEntity = new AccessEntity();
        accessEntity.setIdAccess(1);
        accessEntity.setAccessName("ROLE_USER");
        accessEntity.setUsersEntities(null);
        return accessEntity;
    }
}

