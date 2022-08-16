package com.feedbackcontinuos;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.feedbackcontinuos.dto.UsersCreateDTO;
import com.feedbackcontinuos.dto.UsersDTO;
import com.feedbackcontinuos.entity.AccessEntity;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.enums.Role;
import com.feedbackcontinuos.repository.UsersRepository;
import com.feedbackcontinuos.service.UsersService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
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
        usersEntity.setUserName("Bruno Rodrigues");
        usersEntity.setUserRole("Desenvolvedor de Software");
        usersEntity.setEmail("bruno.rodrigues@dbccompany.com.br");
        usersEntity.setUserPassword("abc@123");
        usersEntity.setAvatar(null);
        return usersEntity;
    }
    private static UsersCreateDTO getUsersCreateDTO(){
        UsersCreateDTO usersCreateDTO = new UsersCreateDTO();
        usersCreateDTO.setUserName("Bruno Rodrigues");
        usersCreateDTO.setUserRole(Role.DESENVOLVEDOR_DE_SOFTWARE);
        usersCreateDTO.setEmail("bruno.rodrigues@dbccompany.com.br");
        usersCreateDTO.setUserPassword("abc@123");
        return usersCreateDTO;
    }
}
