package com.feedbackcontinuos;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.feedbackcontinuos.dto.UserFullDTO;
import com.feedbackcontinuos.dto.UsersCreateDTO;
import com.feedbackcontinuos.dto.UsersDTO;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
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
    public void deveTestarCriarUsuarioComSucesso(){
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
        UsersEntity usersEntity = getUsersEntity();

        List<UsersEntity> usersEntities = List.of(usersEntity);

        criarUsuarioLogado();

        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(usersEntity));

        usersService.findAll();

        assertNotNull(usersEntities);
    }

    @Test
    public void deveTestarFindByEmailComSucesso(){
        UsersEntity usersEntity = getUsersEntity();

        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(usersEntity));

        usersService.findByEmail(anyString());

        assertNotNull(usersEntity);
    }

    @Test
    public void deveTestarGetById() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();
        usersEntity.setAvatar(new byte[]{0,1,2});

        criarUsuarioLogado();

        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(usersEntity));

        usersEntity.setAvatar(Base64.getEncoder().encode(usersEntity.getAvatar()));

        usersService.getById();

        assertNotNull(usersEntity);
    }

    @Test
    public void deveTestarGetByIdUser() throws RegraDeNegocioException {
        UsersEntity usersEntity = getUsersEntity();
        usersEntity.setAvatar(new byte[]{0,1,2});

        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(usersEntity));

        usersService.getByIdUser(anyInt());

        assertNotNull(usersEntity);
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
