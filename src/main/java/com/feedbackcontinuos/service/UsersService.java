package com.feedbackcontinuos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.*;
import com.feedbackcontinuos.entity.AccessEntity;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import com.feedbackcontinuos.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;
    private final ObjectMapper objectMapper;
    private final AccessEntity accessEntity =
            new AccessEntity(1, "ROLE_USER", null);

    public UsersDTO create(UsersCreateDTO usersCreateDTO) throws RegraDeNegocioException {
        if (usersRepository.existsByEmail(usersCreateDTO.getEmail())) {
            throw new RegraDeNegocioException("Usuário já possui cadastro no sistema");
        } else {
            UsersEntity user = objectMapper.convertValue(usersCreateDTO, UsersEntity.class);
            user.setAccessEntity(accessEntity);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String senhaCrypt = passwordEncoder.encode(usersCreateDTO.getUserPassword());
            user.setUserPassword(senhaCrypt);
            user.setUserRole(usersCreateDTO.getUserRole().getDescription());
            usersRepository.save(user);
            return objectMapper.convertValue(user, UsersDTO.class);
        }
    }

    public void uploadFile(Integer id, Optional<MultipartFile> file) throws RegraDeNegocioException, IOException {
        if (file.isPresent()) {
            UsersEntity user = findById(id);
            byte[] byteArray = file.get().getBytes();
            user.setAvatar(Base64.getEncoder().encodeToString(byteArray));
            usersRepository.save(user);
        }
    }

//    public void UpdatePassword(String email){
//        Optional<UsersEntity> user = fi
//    }

    public PageDTO<UserWithNameAndAvatarDTO> findAll(Integer page, Integer register) throws RegraDeNegocioException {
        Pageable pageable = PageRequest.of(page, register);
        Page<UsersEntity> page1 = usersRepository.page(getLoggedUser().getIdUser(), pageable);
        List<UserWithNameAndAvatarDTO> user = page1.getContent().stream()
                .map(usersEntity -> objectMapper.convertValue(usersEntity, UserWithNameAndAvatarDTO.class))
                .toList();
        return new PageDTO<>(page1.getTotalElements(), page1.getTotalPages(), page, register, user);
    }

    public void delete(Integer id) throws RegraDeNegocioException {
        UsersEntity user = findById(id);
        usersRepository.delete(user);
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


    public UserFullDTO getById() throws RegraDeNegocioException {
        UsersEntity user = getLoggedUser();
        return objectMapper.convertValue(user, UserFullDTO.class);
    }

    public UserFullDTO getByIdUser(Integer id) throws RegraDeNegocioException {
        UsersEntity user = findById(id);
        return objectMapper.convertValue(user, UserFullDTO.class);
    }
}
