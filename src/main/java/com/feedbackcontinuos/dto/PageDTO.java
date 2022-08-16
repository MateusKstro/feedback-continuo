package com.feedbackcontinuos.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDTO<T> {
    private Long totalElements;
    private Integer totalPages;
    private Integer page;
    private Integer size;
    private List<T> content;
}
