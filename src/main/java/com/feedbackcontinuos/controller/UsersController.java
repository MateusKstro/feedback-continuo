package com.feedbackcontinuos.controller;

import com.feedbackcontinuos.dto.UsersCreateDTO;
import com.feedbackcontinuos.dto.UsersDTO;
import com.feedbackcontinuos.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Validated
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping
    public UsersDTO post(@RequestBody UsersCreateDTO usersCreateDTO,
                         @RequestBody MultipartFile avatar) throws IOException {
        return usersService.create(usersCreateDTO, avatar);
    }
}
