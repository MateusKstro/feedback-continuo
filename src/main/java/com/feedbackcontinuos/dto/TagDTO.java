package com.feedbackcontinuos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TagDTO {

    @Schema(description = "nome da tag")
    private String name;
}
