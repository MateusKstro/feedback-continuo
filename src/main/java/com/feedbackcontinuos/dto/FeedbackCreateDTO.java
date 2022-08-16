package com.feedbackcontinuos.dto;

import com.feedbackcontinuos.enums.Tags;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
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

    @NotEmpty
    @Schema(description = "id de quem recebeu o feedback")
    private Integer feedbackIdUser;

    @NotEmpty
    @Schema(description = "Lista de tags")
    private List<Tags> tagsList;
}
