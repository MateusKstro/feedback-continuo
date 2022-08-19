package com.feedbackcontinuos.controller;

import com.feedbackcontinuos.dto.*;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import com.feedbackcontinuos.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
@Validated
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
        @ApiResponse(responseCode = "404", description = "Feedback não encontrado"),
        @ApiResponse(responseCode = "415", description = "Formato do payload não suportado pela requisição"),
        @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
})
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "Criar um feedback", description = "Cria um feedback")
    @ApiResponse(responseCode = "200", description = "Cria um feedback")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid FeedbackCreateDTO feedbackCreateDTO) throws RegraDeNegocioException {
        feedbackService.create(feedbackCreateDTO);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Atualizar a visibilidade do feedback", description = "Atualiza a visibilidade do feedback")
    @ApiResponse(responseCode = "200", description = "Atualiza a visibilidade do feedback")
    @PutMapping
    public void updateFeedback(@RequestParam("idFeedback") Integer idFeedback,
                               @RequestParam("publico") boolean publico) {
        feedbackService.updateFeedback(idFeedback, publico);
    }
    @Operation(summary = "Gerar um page de feedbacks enviados", description = "Gera um page de feedbacks enviados")
    @ApiResponse(responseCode = "200", description = "Gera um page de feedbacks enviados")
    @GetMapping("/gived")
    public ResponseEntity<PageDTO<FeedbackGivedDTO>> getGivenFeedback(@RequestParam Integer page) throws RegraDeNegocioException{
        return ResponseEntity.ok(feedbackService.getGivedFeedbacks(page));
    }

    @Operation(summary = "Gerar um page de feedbacks recebidos", description = "Gera um page de feedbacks recebidos")
    @ApiResponse(responseCode = "200", description = "Gera um page de feedbacks recebidos")
    @GetMapping("/receveid")
    public ResponseEntity<PageDTO<FeedbackRecivedDTO>> getReceveidFeedback(@RequestParam Integer page) throws RegraDeNegocioException {
        return ResponseEntity.ok(feedbackService.getReceivedFeedbacks(page));
    }

    @Operation(summary = "Gerar um page de feedbacks enviados por Id do User", description = "Gera um page de feedbacks enviados por Id do User")
    @ApiResponse(responseCode = "200", description = "Gera um page de feedbacks enviados por Id do User")
    @GetMapping("/gived-por-id")
    public ResponseEntity<PageDTO<FeedbackGivedDTO>> getGivenFeedbackIdUser(@RequestParam Integer page, Integer id){
        return ResponseEntity.ok(feedbackService.getGivedFeedbacksIdUser(page, id));
    }

    @Operation(summary = "Gerar um page de feedbacks recebidos por Id do User", description = "Gera um page de feedbacks recebidos por Id do User")
    @ApiResponse(responseCode = "200", description = "Gera um page de feedbacks recebidos por Id do User")
    @GetMapping("/receveid-por-id")
    public ResponseEntity<PageDTO<FeedbackRecivedDTO>> getReceveIdFeedbackIdUser(@RequestParam Integer page, Integer id){
        return ResponseEntity.ok(feedbackService.getReceivedFeedbacksIdUser(page, id));
    }
}
