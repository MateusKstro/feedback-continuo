package com.feedbackcontinuos.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.TagDTO;
import com.feedbackcontinuos.entity.TagEntity;
import com.feedbackcontinuos.enums.Tags;
import com.feedbackcontinuos.exceptions.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagsService {

    private final TagRepository tagRepository;

    private final ObjectMapper objectMapper;

    public List<TagDTO> getAllTags(){
        return tagRepository.findAll().stream()
                .map(tagEntity -> objectMapper.convertValue(tagEntity, TagDTO.class))
                .toList();
    }

    public TagEntity findTag (Tags tags) {
        Optional<TagEntity> buscarTag = tagRepository.findByName(tags.getName());
        if(buscarTag.isEmpty()){
            return tagRepository.saveAndFlush(TagEntity.builder().name(tags.getName()).build());
        }
        return buscarTag.get();
    }
}
