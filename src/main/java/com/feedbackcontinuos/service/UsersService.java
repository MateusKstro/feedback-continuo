package com.feedbackcontinuos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedbackcontinuos.dto.UsersCreateDTO;
import com.feedbackcontinuos.dto.UsersDTO;
import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public UsersDTO create(UsersCreateDTO usersCreateDTO, MultipartFile avatar) throws IOException {
        UsersEntity user = objectMapper.convertValue(usersCreateDTO, UsersEntity.class);
        if (!avatar.isEmpty()){
            byte [] byteArray = avatar.getBytes();
            user.setAvatar(byteArray.toString());
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String senhaCrypt = passwordEncoder.encode(usersCreateDTO.getUserPassword());
        user.setUserPassword(senhaCrypt);
        usersRepository.save(user);
        return objectMapper.convertValue(user, UsersDTO.class);
    }
    
}
