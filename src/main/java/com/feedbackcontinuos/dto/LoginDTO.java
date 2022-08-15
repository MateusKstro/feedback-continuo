package com.feedbackcontinuos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginDTO {

    @NotNull
    @Schema(description = "Email do Usuário")
    private String login;
    @NotNull
    @Schema(description = "Senha do Usuário")
    private String senha;
}
