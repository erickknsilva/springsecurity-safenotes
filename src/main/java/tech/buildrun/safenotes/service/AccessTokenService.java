package tech.buildrun.safenotes.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import tech.buildrun.safenotes.config.JwtConfig;
import tech.buildrun.safenotes.entity.Role;
import tech.buildrun.safenotes.entity.Scope;
import tech.buildrun.safenotes.entity.User;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccessTokenService {

    private final JwtConfig jwtConfig;
    private final JwtEncoder jwtEncoder;

    public AccessTokenService(JwtConfig jwtConfig, JwtEncoder jwtEncoder) {
        this.jwtConfig = jwtConfig;
        this.jwtEncoder = jwtEncoder;
    }


    public Jwt generateAccessToken(User user, UUID familyId) {

        // 1 - recuperar as roles do usuario
        var roles = getRoles(user);

        //2 - recuperar os scopes da roles e para associas-las ao usuario
        var scopes = getScopes(user);
        var tokenVersion = user.getTokenVersion();

        //3 - montar o jwt com as claims necessarias e gerar o token
        var claims = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .issuer(jwtConfig.getIssuer())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtConfig.getExpiresIn()))
                .claim("username", user.getUsername())
                .claim("family_id", familyId.toString())
                .claim("version", tokenVersion)
                .claim("roles", roles)
                .claim("scope", scopes).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }

    private static Set<String> getScopes(User user) {
        Set<Scope> mergedScopes = user.getRoles()
                .stream()
                .map(Role::getScopes)
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(HashSet::new));

        mergedScopes.addAll(user.getScopes());

        return mergedScopes
                .stream()
                .map(Scope::getName)
                .collect(Collectors.toSet());
    }


    private static Set<String> getRoles(User user) {

        return user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

}
