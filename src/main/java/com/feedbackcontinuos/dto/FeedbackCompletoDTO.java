package com.feedbackcontinuos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FeedbackCompletoDTO {

    @Schema(description = "id do feedback")
    private Integer feedbackId;

    @Schema(description = "Nome do user")
    private String userName;

    @Schema(description = "Foto de perfil")
    private String avatar;

    @Schema(description = "Mensagem do feedback")
    private String message;

    @Schema(description = "Lista de tags")
    private List<String> tags;

    @Schema(description = "Data de criacao")
    private LocalDateTime dataEHora;
}
