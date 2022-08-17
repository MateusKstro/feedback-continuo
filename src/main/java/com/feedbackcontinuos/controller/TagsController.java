package com.feedbackcontinuos.controller;


import com.feedbackcontinuos.dto.TagDTO;
import com.feedbackcontinuos.service.TagsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagsController {

    private final TagsService tagsService;

    @GetMapping
    public ResponseEntity<List<TagDTO>> getAll(){
        return ResponseEntity.ok(tagsService.getAllTags());
    }
}
