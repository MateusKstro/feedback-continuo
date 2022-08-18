package com.feedbackcontinuos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackCompletoDTO {

    @Schema(description = "id do feedback")
    private Integer idFeedback;

    @Schema(description = "id de quem enviou")
    private Integer userId;

    @Schema(description = "Foto de perfil")
    private String avatar;

    @Schema(description = "Mensagem do feedback")
    private String message;

    @Schema(description = "id de quem recebeu")
    private Integer idUserRecevid;

    @Schema(description = "Lista de tags")
    private List<TagCreateDTO> tagsList;

    @Schema(description = "Data de criacao")
    private LocalDateTime dataEHora;
}
