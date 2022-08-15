package com.feedbackcontinuos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.UsersCreateDTO;
import com.feedbackcontinuos.dto.UsersDTO;
import com.feedbackcontinuos.entity.AccessEntity;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import com.feedbackcontinuos.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {


    private final UsersRepository usersRepository;
    private final ObjectMapper objectMapper;

    public UsersDTO create(UsersCreateDTO usersCreateDTO) {
        UsersEntity user = objectMapper.convertValue(usersCreateDTO, UsersEntity.class);
        user.setAccessEntity(new AccessEntity(1, "ROLE_USER", null));
//        if (!avatar.isEmpty()){
//            byte[] byteArray = avatar.getBytes();
//            user.setAvatar(byteArray);
//        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String senhaCrypt = passwordEncoder.encode(usersCreateDTO.getUserPassword());
        user.setUserPassword(senhaCrypt);
        user.setUserRole(usersCreateDTO.getUserRole().getDescription());
        usersRepository.save(user);
        return objectMapper.convertValue(user, UsersDTO.class);
    }

    public Optional<UsersEntity> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    public UsersEntity findById(Integer id) throws RegraDeNegocioException {
        return usersRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));
    }

    public UsersEntity getLoggedUser() throws RegraDeNegocioException {
        return findById(getIdLoggedUser());
    }

    public Integer getIdLoggedUser() {
        return (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public UsersDTO getById() throws RegraDeNegocioException {
        return objectMapper.convertValue(getLoggedUser(), UsersDTO.class);
    }
}
