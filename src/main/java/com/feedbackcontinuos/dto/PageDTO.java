package com.feedbackcontinuos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDTO<T> {

    @Schema(description = "Total de elementos por pagina")
    private Long totalElements;

    @Schema(description = "Total de paginas")
    private Integer totalPages;

    @Schema(description = "Numero da pagina")
    private Integer page;

    @Schema(description = "Numero de elementos")
    private Integer size;

    @Schema(description = "Lista de conteudos")
    private List<T> content;
}
