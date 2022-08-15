package com.feedbackcontinuos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.LoginDTO;
import com.feedbackcontinuos.dto.UsersCreateDTO;
import com.feedbackcontinuos.dto.UsersDTO;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import com.feedbackcontinuos.security.TokenService;
import com.feedbackcontinuos.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UsersController {
    private final UsersService usersService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Gerar um token para acesso ao sistema", description = "Retorna um token válido")
    @ApiResponse(responseCode = "200", description = "Retorna um token válido")
    @PostMapping("/login")
    public String auth(@RequestBody @Valid LoginDTO login) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        login.getLogin(),
                        login.getSenha()
                );
        Authentication authentication = authenticationManager
                .authenticate(usernamePasswordAuthenticationToken);
        log.info("Autenticado com sucesso!");
        Object usuarioLogado = authentication.getPrincipal();
        UsersEntity usuario = (UsersEntity) usuarioLogado;
        return tokenService.getToken(usuario);
    }
    @PostMapping(value = "/create")
    public UsersDTO post(@RequestBody @Valid UsersCreateDTO usersCreateDTO){
        return usersService.create(usersCreateDTO);
    }
    @PutMapping(value = "/update-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void update(@RequestPart("id") String id,
                       @RequestPart(value = "file", required = false) Optional<MultipartFile> file) throws RegraDeNegocioException, IOException {
        Integer idUser = Integer.valueOf(id);
        usersService.uploadFile(idUser, file);
    }
}
