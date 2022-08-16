package com.feedbackcontinuos.dto;

import com.feedbackcontinuos.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UsersCreateDTO {

    @NotEmpty
    @Schema(description = "Nome do Usuário")
    private String userName;
    @NotNull
    @Schema(description = "Função do Usuário")
    private Role userRole;
    @NotEmpty
    @Pattern(regexp = "^.*(@dbccompany.com.br).*$", message = "O email deve conter @dbccompany.com.br")
    @Schema(description = "Email do Usuário")
    private String email;
    @NotEmpty
    @Pattern(regexp = "^(?=.*[@!#$%^&*()/\\\\])[@!#$%^&*()/\\\\a-zA-Z0-9]{4,16}", message = "Senha do Usuário, deve contar no mínimo 4 digitos (letras, número e caracter especial)")
    @Schema(description = "Senha do Usuário")
    private String userPassword;
}
