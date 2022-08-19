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
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
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
    public void deveTetarPageDeFeedbacksReceived() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();
        List<FeedBackEntity> feedBackEntityList = new ArrayList<>();
        Page<FeedBackEntity> feedBackEntityPage = new PageImpl<>(feedBackEntityList);
        when(usersService.getLoggedUser()).thenReturn(usersEntity);
        when(feedBackRepository.findByUserIdRecived(anyInt(), any(Pageable.class))).thenReturn(feedBackEntityPage);
        feedbackService.getReceivedFeedbacks(0);
        assertNotNull(feedBackEntityPage);
    }

    @Test
    public void deveTetarPageDeFeedbacksGived() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();
        List<FeedBackEntity> feedBackEntityList = new ArrayList<>();
        Page<FeedBackEntity> feedBackEntityPage = new PageImpl<>(feedBackEntityList);
        when(usersService.getLoggedUser()).thenReturn(usersEntity);
        when(feedBackRepository.findByUserIdGived(anyInt(), any(Pageable.class))).thenReturn(feedBackEntityPage);
        feedbackService.getGivedFeedbacks(0);
        assertNotNull(feedBackEntityPage);
    }

    @Test
    public void deveTetarPageDeFeedbacksGivedIdUser()  {
        Integer idFind = 1;
        List<FeedBackEntity> feedBackEntityList = new ArrayList<>();
        feedBackEntityList.add(FeedBackEntity.builder().tagsList(List.of(TagEntity.builder().build())).build());
        Page<FeedBackEntity> feedBackEntityPage = new PageImpl<>(feedBackEntityList);
        when(feedBackRepository.findByUserIdGived(anyInt(), any(Pageable.class))).thenReturn(feedBackEntityPage);
        feedbackService.getGivedFeedbacksIdUser(0, idFind);
        assertNotNull(feedBackEntityPage);
    }

    @Test
    public void deveTetarPageDeFeedbacksReceivedIdUser()  {
        Integer idFind = 2;
        List<FeedBackEntity> feedBackEntityList = new ArrayList<>();
        feedBackEntityList.add(FeedBackEntity.builder().tagsList(List.of(TagEntity.builder().build())).build());
        Page<FeedBackEntity> feedBackEntityPage = new PageImpl<>(feedBackEntityList);
        when(feedBackRepository.findByUserIdRecived(anyInt(), any(Pageable.class))).thenReturn(feedBackEntityPage);
        feedbackService.getReceivedFeedbacksIdUser(0, idFind);
        assertNotNull(feedBackEntityPage);
    }

    @Test
    public void deveTestarPublicoFeedBack(){
        Optional<FeedBackEntity> feedBackEntity = Optional.of(getFeedBackEntity());

        when(feedBackRepository.findById(anyInt())).thenReturn(feedBackEntity);

        feedbackService.updateFeedback(2, true);

        assertNotNull(feedBackEntity);
    }

    private final FeedbackCreateDTO getFeedbackCreatDTOParaSiMemso() {
        FeedbackCreateDTO feedbackCreateDTO = new FeedbackCreateDTO();
        feedbackCreateDTO.setMessage("teste");
        feedbackCreateDTO.setFeedbackUserId(1);
        feedbackCreateDTO.setAnonymous(false);
        feedbackCreateDTO.setTagsList(List.of(getTagCreateDTO()));
        return feedbackCreateDTO;
    }

    private final FeedbackCreateDTO getFeedbackCreatDTO() {
        FeedbackCreateDTO feedbackCreateDTO = new FeedbackCreateDTO();
        feedbackCreateDTO.setMessage("teste");
        feedbackCreateDTO.setFeedbackUserId(2);
        feedbackCreateDTO.setAnonymous(false);
        feedbackCreateDTO.setTagsList(List.of(getTagCreateDTO()));
        return feedbackCreateDTO;
    }

    private final TagCreateDTO getTagCreateDTO() {
        TagCreateDTO tagCreateDTO = new TagCreateDTO();
        tagCreateDTO.setName("JAVA");
        tagCreateDTO.setName("JAVASCRIPT");
        return tagCreateDTO;
    }

    private final FeedBackEntity getFeedBackEntity(){
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

    private final UsersEntity getUsersEntity() {
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

    private final AccessEntity getAccessEntity() {
        AccessEntity accessEntity = new AccessEntity();
        accessEntity.setIdAccess(1);
        accessEntity.setAccessName("ROLE_USER");
        accessEntity.setUsersEntities(null);
        return accessEntity;
    }


}

