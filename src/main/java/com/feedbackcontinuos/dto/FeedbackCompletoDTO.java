package com.feedbackcontinuos.dto;

import com.feedbackcontinuos.entity.UsersEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FeedbackCompletoDTO {

    @Schema(description = "mensagem do feedback")
    private String message;
    @Schema(description = "validando mensagem anonima")
    private Boolean anonymous;
    @Schema(description = "id de quem recebeu o feedback")
    private UserWithNameAndAvatarDTO feedbackUserId;
    @Schema(description = "Lista de tags")
    private List<TagCreateDTO> tagsList;
    @Schema(description = "id do feedback")
    private Integer idFeedback;
    @Schema(description = "id do user que fez o feedback")
    private Integer userId;
    @Schema(description = "data de criacao")
    private LocalDateTime dataEHora;

}
