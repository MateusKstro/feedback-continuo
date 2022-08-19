package com.feedbackcontinuos;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.feedbackcontinuos.dto.TagCreateDTO;
import com.feedbackcontinuos.dto.TagDTO;
import com.feedbackcontinuos.entity.TagEntity;
import com.feedbackcontinuos.repository.TagRepository;
import com.feedbackcontinuos.service.TagsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TagsServiceTest {

    @InjectMocks
    private TagsService tagsService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TagRepository tagRepository;

    @Before
    public void init(){
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(tagsService, "objectMapper", objectMapper);
    }


    @Test
    public void deveRetornarTag(){
        TagEntity tagEntity =  getTag();
        when(tagRepository.findByName(anyString())).thenReturn(Optional.ofNullable(tagEntity));
        TagEntity tags = tagsService.tagCreate(getTagCreateDTO());
        assertEquals(tagEntity, tags);
    }

    @Test
    public void deveCriarTag(){
        when(tagRepository.findByName(anyString())).thenReturn(Optional.empty());
        tagsService.tagCreate(getTagCreateDTO());
        verify(tagRepository, times(1)).save(any(TagEntity.class));
    }

    @Test
    public void deveListarTodasTags(){
        TagEntity tagEntity =  getTag();
        List<TagEntity> tags = List.of(tagEntity);
        when(tagRepository.findAll()).thenReturn(tags);
        List<TagDTO> tag = tagsService.getAllTags();
        assertEquals(tags.size(), tag.size());
    }


    private TagEntity getTag(){
        TagEntity tagEntity = new TagEntity();
        tagEntity.setIdTag(1);
        tagEntity.setName("JAVA");
        return tagEntity;
    }

    private TagCreateDTO getTagCreateDTO(){
        TagCreateDTO tagCreateDTO = new TagCreateDTO();
        tagCreateDTO.setName("TESTE");
        return tagCreateDTO;
    }

}
