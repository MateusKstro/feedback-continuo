package com.feedbackcontinuos.security;

import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UsersService usersService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UsersEntity> usuarioOptional = usersService.findByEmail(email);
        return usuarioOptional
                .orElseThrow(() -> new UsernameNotFoundException("Usuario inválido"));
    }
}
