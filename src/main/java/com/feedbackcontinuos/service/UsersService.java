package com.feedbackcontinuos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.UsersCreateDTO;
import com.feedbackcontinuos.dto.UsersDTO;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import com.feedbackcontinuos.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {


    private final UsersRepository usersRepository;
    private final ObjectMapper objectMapper;

    public UsersDTO create(UsersCreateDTO usersCreateDTO, MultipartFile avatar) throws IOException {
        UsersEntity user = objectMapper.convertValue(usersCreateDTO, UsersEntity.class);
        if (!avatar.isEmpty()){
            byte [] byteArray = avatar.getBytes();
            user.setAvatar(byteArray);
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String senhaCrypt = passwordEncoder.encode(usersCreateDTO.getUserPassword());
        user.setUserPassword(senhaCrypt);
        usersRepository.save(user);
        return objectMapper.convertValue(user, UsersDTO.class);
    }

    public Optional<UsersEntity> findByEmail(String email){
        return usersRepository.findByEmail(email);
    }

    public Integer getLoggedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return (Integer) authentication.getPrincipal();
        }
        return null;
    }

    public UsersDTO getLoggedUser() throws RegraDeNegocioException {
        return findByIdUser(getLoggedUserId());
    }

    public UsersDTO findByIdUser(Integer idUsuario) throws RegraDeNegocioException {
        UsersEntity usuario = usersRepository.findById(idUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("usuario não encontrado"));
        return objectMapper.convertValue(usuario, UsersDTO.class);
    }

    public UsersEntity getLogedUserEntity() throws RegraDeNegocioException {
        return usersRepository.findById(getLoggedUserId())
                .orElseThrow(() -> new RegraDeNegocioException("Erro inesperado."));
    }
    public UsersEntity getUserById(Integer id) throws RegraDeNegocioException {
        return usersRepository.findById(id).orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));
    }
}
