package com.feedbackcontinuos.controller;

import com.feedbackcontinuos.dto.LoginDTO;
import com.feedbackcontinuos.dto.UsersCreateDTO;
import com.feedbackcontinuos.dto.UsersDTO;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.security.TokenService;
import com.feedbackcontinuos.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    @PostMapping("/create")
    public UsersDTO post(@RequestBody UsersCreateDTO usersCreateDTO) {
        return usersService.create(usersCreateDTO);
    }
}
