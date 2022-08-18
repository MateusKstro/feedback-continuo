package com.feedbackcontinuos.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.TagCreateDTO;
import com.feedbackcontinuos.dto.TagDTO;
import com.feedbackcontinuos.entity.TagEntity;
import com.feedbackcontinuos.enums.Tags;
import com.feedbackcontinuos.repository.TagRepository;
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
    public TagEntity tagCreate(TagCreateDTO tag) {
        Optional<TagEntity> buscar = tagRepository.findByName(tag.getName().toUpperCase().replace(" ", "_"));
        if (buscar.isEmpty()) {
            return tagRepository.save(TagEntity.builder().name(tag.getName().toUpperCase().replace(" ", "_")).build());
        }
        return buscar.get();
    }
}
