package com.feedbackcontinuos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserWithNameAndAvatarDTO {

    @Schema(description = "Nome do Usuário")
    private String userNamer;
    @Schema(description = "Função do Usuário")
    private String userRole;
    @Schema(description = "Imagem do Usuário")
    private String avatar;
}

