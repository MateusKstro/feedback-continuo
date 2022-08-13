package com.feedbackcontinuos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.UsersCreateDTO;
import com.feedbackcontinuos.dto.UsersDTO;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
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
        usersRepository.save(user);
        return objectMapper.convertValue(user, UsersDTO.class);
    }

    public Optional<UsersEntity> findByEmail(String email){
        return usersRepository.findByEmail(email);
    }
}
