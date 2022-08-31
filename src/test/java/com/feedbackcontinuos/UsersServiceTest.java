package com.feedbackcontinuos;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.feedbackcontinuos.dto.*;
import com.feedbackcontinuos.entity.AccessEntity;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.enums.Role;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import com.feedbackcontinuos.repository.UsersRepository;
import com.feedbackcontinuos.service.UsersService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UsersServiceTest {

    @InjectMocks
    private UsersService usersService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UsersRepository usersRepository;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(usersService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarCriarUsuarioComSucesso() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();
        when(usersRepository.save(any(UsersEntity.class))).thenReturn(usersEntity);
        UsersDTO usersDTO = usersService.create(getUsersCreateDTO());
        assertNotNull(usersDTO);
        assertNull(usersDTO.getIdUser());
    }


    @Test
    public void deveTestarBuscarUsuarioPeloIdComSucesso() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();
        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(usersEntity));
        UsersEntity usersEntity1 = usersService.findById(anyInt());
        assertNotNull(usersEntity1);
        assertEquals(usersEntity.getUsername(), usersEntity1.getUsername());
        assertEquals(usersEntity.getUserRole(), usersEntity1.getUserRole());
    }

    @Test
    public void deveTestarFindAllComSucesso() throws RegraDeNegocioException {

        List<UsersEntity> users = List.of(getUsersEntity());
        Page<UsersEntity> page = new PageImpl<>(users);
        UsersEntity users1 = getUsersEntity1();

        criarUsuarioLogado();
        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(users1));

        when(usersRepository.paginarUsuariosEmCrescente(anyInt(),any(Pageable.class))).thenReturn(page);

        PageDTO<UserWithNameAndAvatarDTO> paginaDeUsuarios = usersService
                .findAll(0, 100);

        assertFalse(paginaDeUsuarios.getContent().isEmpty());
        assertEquals(1, paginaDeUsuarios.getContent().size());
        assertEquals(users1.getName(), paginaDeUsuarios.getContent().get(0).getName());
        assertEquals(users1.getAvatar(), paginaDeUsuarios.getContent().get(0).getAvatar());
    }

    @Test
    public void deveTestarFindByEmailComSucesso(){
        UsersEntity usersEntity = getUsersEntity();
        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(usersEntity));
        usersService.findByEmail(anyString());
        assertNotNull(usersEntity);
    }

    @Test
    public void deveTestarGetByIdComSucesso() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();
        criarUsuarioLogado();
        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(usersEntity));
        usersService.getById();
        assertNotNull(usersEntity);
    }

    @Test
    public void deveTestarGetByIdUserComSucesso() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();
        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(usersEntity));
        usersService.getByIdUser(anyInt());
        assertNotNull(usersEntity);
    }

    @Test()
    public void deveTestarDeleteComSucesso() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();
        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(usersEntity));
        usersService.delete(usersEntity.getIdUser());
        assertNotNull(usersEntity);
    }

    @Test
    public void deveTestarUploadComSucesso() throws RegraDeNegocioException, IOException {
        UsersEntity usersEntity = getUsersEntity();
        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(usersEntity));
        MockMultipartFile file = new MockMultipartFile("file", "teste.txt", "text/plain", "teste".getBytes());
        usersService.uploadFile(Optional.of(file));
        assertFalse(usersEntity.getAvatar().isEmpty());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestardeveTestarCriarUsuario() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();
        UsersCreateDTO usersCreateDTO = getUsersCreateDTO();
        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(usersEntity));
        usersService.create(getUsersCreateDTO());
        assertNotNull(usersCreateDTO);
        assertSame(usersCreateDTO.getEmail(), "bruno.rodrigues@dbccompany.com.br");
        assertNotNull(usersCreateDTO.getEmail());
    }

    @Test
    public void deveTestarListAll() throws RegraDeNegocioException {
        List<UsersEntity> users = List.of(getUsersEntity());
        UsersEntity users1 = getUsersEntity1();

        criarUsuarioLogado();
        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(users1));

        List<UserWithNameAndAvatarDTO> list = usersService.getAllUsers();

        assertNotNull(users);
    }

    private static AccessEntity getAccessEntity(){
        AccessEntity accessEntity = new AccessEntity();
        accessEntity.setIdAccess(1);
        accessEntity.setAccessName("ROLE_USER");
        accessEntity.setUsersEntities(null);
        return accessEntity;
    }

    private static UsersEntity getUsersEntity() {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setIdUser(1);
        usersEntity.setAccessEntity(getAccessEntity());
        usersEntity.setName("Bruno Rodrigues");
        usersEntity.setUserRole("Desenvolvedor de Software");
        usersEntity.setEmail("bruno.rodrigues@dbccompany.com.br");
        usersEntity.setUserPassword("abc@123");
        usersEntity.setAvatar(null);
        return usersEntity;
    }

    private static UsersEntity getUsersEntity1() {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setIdUser(2);
        usersEntity.setAccessEntity(getAccessEntity());
        usersEntity.setName("Bruno Rodrigues");
        usersEntity.setUserRole("Desenvolvedor de Software");
        usersEntity.setEmail("bruno.rodrigues@dbccompany.com.br");
        usersEntity.setUserPassword("abc@123");
        usersEntity.setAvatar(null);
        return usersEntity;
    }

    private static UsersCreateDTO getUsersCreateDTO(){
        UsersCreateDTO usersCreateDTO = new UsersCreateDTO();
        usersCreateDTO.setName("Bruno Rodrigues");
        usersCreateDTO.setUserRole(Role.DESENVOLVEDOR_DE_SOFTWARE);
        usersCreateDTO.setEmail("bruno.rodrigues@dbccompany.com.br");
        usersCreateDTO.setUserPassword("abc@123");
        return usersCreateDTO;
    }


    private void criarUsuarioLogado() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        123,
                        null
                );

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
