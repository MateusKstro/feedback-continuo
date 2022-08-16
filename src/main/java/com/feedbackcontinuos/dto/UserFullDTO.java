package com.feedbackcontinuos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserFullDTO {

    @Schema(description = "Id do Usuário")
    private Integer idUser;
    @Schema(description = "Nome do Usuário")
    private String userNamer;
    @Schema(description = "Cargo do Usuário")
    private String userRole;
    @Schema(description = "Email do Usuário")
    private String email;
    @Schema(description = "Foto do Usuário")
    private String avatar;
}
