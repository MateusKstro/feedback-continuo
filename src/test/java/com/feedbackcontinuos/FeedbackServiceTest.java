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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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


    @Test
    public void deveTestarCreateFeedbackByLoggedUserComSucesso() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();
        FeedbackCreateDTO feedbackCreateDTO = getFeedbackCreatDTO();

        when(usersService.getLoggedUser()).thenReturn(usersEntity);
        when(tagsService.tagCreate(any())).thenReturn(TagEntity.builder().build());

        feedbackService.create(feedbackCreateDTO);
    }

    @Test
    public void deveTetarPageDeFeedbacksReceived() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();

        List<FeedBackEntity> feedBackEntityList = new ArrayList<>();
        Page<FeedBackEntity> feedBackEntityPage = new PageImpl<>(feedBackEntityList);

        when(feedBackRepository.findByFeedbackUserId(any(), anyInt())).thenReturn(feedBackEntityPage);
        when(usersService.getLoggedUser()).thenReturn(usersEntity);


        feedbackService.getReceivedFeedbacks(0);

        assertEquals(feedBackEntityPage, feedBackRepository.findByFeedbackUserId(any(), anyInt()));
    }

    @Test
    public void deveTetarPageDeFeedbacksGived() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();

        List<FeedBackEntity> feedBackEntityList = new ArrayList<>();
        Page<FeedBackEntity> feedBackEntityPage = new PageImpl<>(feedBackEntityList);

        when(feedBackRepository.findByUserId(any(), anyInt())).thenReturn(feedBackEntityPage);
        when(usersService.getLoggedUser()).thenReturn(usersEntity);


        feedbackService.getGivedFeedbacks(0);

        assertEquals(feedBackEntityPage, feedBackRepository.findByUserId(any(), anyInt()));
    }

    @Test
    public void deveTetarPageDeFeedbacksGivedIdUser() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();

        Integer idFind = 1;
        List<FeedBackEntity> feedBackEntityList = new ArrayList<>();
        feedBackEntityList.add(FeedBackEntity.builder().tagsList(List.of(TagEntity.builder().build())).build());
        Page<FeedBackEntity> feedBackEntityPage = new PageImpl<>(feedBackEntityList);

        when(feedBackRepository.findByUserId(any(), anyInt())).thenReturn(feedBackEntityPage);
        when(usersService.findById(idFind)).thenReturn(usersEntity);


        feedbackService.getGivedFeedbacksIdUser(0, idFind);

        assertEquals(feedBackEntityPage, feedBackRepository.findByUserId(any(), anyInt()));
    }

    @Test
    public void deveTetarPageDeFeedbacksReceivedIdUser() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();

        Integer idFind = 1;
        List<FeedBackEntity> feedBackEntityList = new ArrayList<>();
        Page<FeedBackEntity> feedBackEntityPage = new PageImpl<>(feedBackEntityList);

        when(feedBackRepository.findByFeedbackUserId(any(), anyInt())).thenReturn(feedBackEntityPage);
        when(usersService.findById(idFind)).thenReturn(usersEntity);


        feedbackService.getReceivedFeedbacksIdUser(0, idFind);

        assertEquals(feedBackEntityPage, feedBackRepository.findByFeedbackUserId(any(), anyInt()));

    }

    private FeedbackCreateDTO getFeedbackCreatDTO() {
        FeedbackCreateDTO feedbackCreateDTO = new FeedbackCreateDTO();
        feedbackCreateDTO.setMessage("teste");
        feedbackCreateDTO.setFeedbackUserId(1);
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

