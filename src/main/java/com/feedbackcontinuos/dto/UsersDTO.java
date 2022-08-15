package com.feedbackcontinuos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsersDTO {

    @Schema(description = "Id do Usuário")
    private Integer idUser;
}

