package com.feedbackcontinuos.controller;


import com.feedbackcontinuos.dto.TagDTO;
import com.feedbackcontinuos.service.TagsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
        @ApiResponse(responseCode = "404", description = "Tag não encontrada"),
        @ApiResponse(responseCode = "415", description = "Formato do payload não suportado pela requisição"),
        @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
})
public class TagsController {

    private final TagsService tagsService;

    @Operation(summary = "Listar tags", description = "Lista todas as tags")
    @ApiResponse(responseCode = "200", description = "Lista todas as tags")
    @GetMapping
    public ResponseEntity<List<TagDTO>> getAll(){
        return ResponseEntity.ok(tagsService.getAllTags());
    }
}
