package com.feedbackcontinuos.dto;

import com.feedbackcontinuos.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsersDTO {

    @Schema(description = "Nome do Usuário")
    private String userNamer;
    @Schema(description = "Função do Usuário")
    private Role userRole;
    @Schema(description = "Imagem do Usuário")
    private String avatar;

}

