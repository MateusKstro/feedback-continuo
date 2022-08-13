package com.feedbackcontinuos.security;

import com.feedbackcontinuos.entity.UsersEntity;
import com.feedbackcontinuos.exceptions.RegraDeNegocioException;
import com.feedbackcontinuos.service.UsersService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final UsersService usersService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    private static final String KEY_CARGO = "roles";

    public String getToken(UsersEntity usersEntity) throws RegraDeNegocioException {
        if (usersEntity.isActive() == false){
            throw new RegraDeNegocioException("Usuário desativado");
        }
        Date now = new Date();
        Date exp = new Date(now.getTime() + Long.valueOf(expiration));

        List<String> cargos = List.of(usersEntity.getUsername());

        String token = Jwts.builder()
                .setIssuer("pessoa-api")
                .claim(Claims.ID, usersEntity.getIdUser())
                .claim(KEY_CARGO, cargos)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return TokenAuthenticationFilter.BEARER + token;
    }

    public UsernamePasswordAuthenticationToken isValid(String token) {
        if (token == null) {
            return null;
        }

        Claims body = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        Integer idUsuario = body.get(Claims.ID, Integer.class);

        if (idUsuario != null){
            List<String> cargos = body.get(KEY_CARGO, List.class);

            List<SimpleGrantedAuthority> cargosGrantedAuthority = cargos.stream()
                    .map(cargo -> new SimpleGrantedAuthority(cargo))
                    .toList();

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(idUsuario, null, cargosGrantedAuthority);

            return usernamePasswordAuthenticationToken;
        }
        return null;
    }

}
