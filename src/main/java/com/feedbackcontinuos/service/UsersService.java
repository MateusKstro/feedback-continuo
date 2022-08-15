package com.feedbackcontinuos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.UserWithNameAndAvatarDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final ObjectMapper objectMapper;
    private final AccessEntity accessEntity =
            new AccessEntity(1, "ROLE_USER", null);

    public UsersDTO create(UsersCreateDTO usersCreateDTO) {
        UsersEntity user = objectMapper.convertValue(usersCreateDTO, UsersEntity.class);
        user.setAccessEntity(accessEntity);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String senhaCrypt = passwordEncoder.encode(usersCreateDTO.getUserPassword());
        user.setUserPassword(senhaCrypt);
        user.setUserRole(usersCreateDTO.getUserRole().getDescription());
        usersRepository.save(user);
        return objectMapper.convertValue(user, UsersDTO.class);
    }

    public void uploadFile(Integer id, Optional<MultipartFile> file) throws RegraDeNegocioException, IOException {
        if (file.isPresent()){
            UsersEntity user = findById(id);
            byte[] byteArray = file.get().getBytes();
            user.setAvatar(byteArray);
            usersRepository.save(user);
        }
    }
    public List<UserWithNameAndAvatarDTO> findAll() throws RegraDeNegocioException {
        List<UsersEntity> users = usersRepository.findAll();
        users.remove(getLoggedUser());
        return users.stream()
                .map(usersEntity -> objectMapper.convertValue(usersEntity, UserWithNameAndAvatarDTO.class))
                .toList();
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
