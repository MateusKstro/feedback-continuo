package com.feedbackcontinuos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FeedbackCreateDTO {

    @NotEmpty
    @Schema(description = "mensagem do feedback")
    private String message;

    @Schema(description = "validando mensagem anonima")
    private Boolean anonymous;

    @NotNull
    @Schema(description = "id de quem recebeu o feedback")
    private Integer feedbackUserId;

    @Schema(description = "Lista de tags")
    private List<TagCreateDTO> tagsList;
}
