package com.feedbackcontinuos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserWithNameAndAvatarDTO {

    @Schema(description = "Id do Usuário")
    private Integer idUser;
    @Schema(description = "Nome do Usuário")
    private String name;
    @Schema(description = "Cargo do Usuário")
    private String userRole;
    @Schema(description = "Imagem do Usuário")
    private String avatar;
}

