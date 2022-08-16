package com.feedbackcontinuos.dto;

import lombok.Data;

@Data
public class UserFullDTO {

    private Integer idUser;
    private String userNamer;
    private String userRole;
    private String email;
    private String avatar;
}
