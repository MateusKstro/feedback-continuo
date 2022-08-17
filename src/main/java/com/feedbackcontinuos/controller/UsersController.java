package com.feedbackcontinuos.controller;

import com.feedbackcontinuos.dto.*;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import com.feedbackcontinuos.security.TokenService;
import com.feedbackcontinuos.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "415", description = "Formato do payload não suportado pela requisição"),
        @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
})
public class UsersController {
    private final UsersService usersService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Gerar um token para acesso ao sistema", description = "Retorna um token válido")
    @ApiResponse(responseCode = "200", description = "Retorna um token válido")
    @PostMapping("/login")
    public ResponseEntity<String> auth(@RequestBody @Valid LoginDTO login) {
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
        return new ResponseEntity<>(tokenService.getToken(usuario), HttpStatus.OK);
    }

    @Operation(summary = "Criar um usuário novo", description = "Cria um usuário")
    @ApiResponse(responseCode = "200", description = "Cria um usuário")
    @PostMapping(value = "/create")
    public ResponseEntity<UsersDTO> post(@RequestBody @Valid UsersCreateDTO usersCreateDTO) {
        return new ResponseEntity<>(usersService.create(usersCreateDTO), HttpStatus.OK);
    }

    @Operation(summary = "Fazer o upload de uma foto de perfil para o usuário", description = "Upload de foto de perfil")
    @ApiResponse(responseCode = "200", description = "Upload de foto de perfil")
    @PutMapping(value = "/update-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> update(@RequestPart("id") String id,
                                       @RequestPart(value = "file", required = false) Optional<MultipartFile> file)
            throws RegraDeNegocioException, IOException {
        Integer idUser = Integer.valueOf(id);
        usersService.uploadFile(idUser, file);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Listar todos os usuários, exceto o que está logado", description = "Lista todos os usuários, exceto o logado")
    @ApiResponse(responseCode = "200", description = "Lista todos os usuários, exceto o logado")
    @GetMapping("/list-all")
    public ResponseEntity<List<UserWithNameAndAvatarDTO>> getAll() throws RegraDeNegocioException {
        return new ResponseEntity<>(usersService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Recuperar o usuário logado", description = "Retorna o usuário logado")
    @ApiResponse(responseCode = "200", description = "Retorna o usuário logado")
    @GetMapping("/recuperar-usuario-logado")
    public UserFullDTO getLoggedUser() throws RegraDeNegocioException {
        return usersService.getById();
    }

    @Operation(summary = "Retornar usuário por id", description = "Retorna o usuário pelo id")
    @ApiResponse(responseCode = "200", description = "Retorna o usuário pelo id")
    @GetMapping("/retornar-usuario")
    public UserFullDTO getIdUser(Integer id) throws RegraDeNegocioException {
        return usersService.getByIdUser(id);
    }
}
