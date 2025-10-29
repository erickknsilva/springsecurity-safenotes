package tech.buildrun.safenotes.utils;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import tech.buildrun.safenotes.service.TokenBlockListService;

@Component
public class JwtBlocklistValidator implements OAuth2TokenValidator<Jwt> {

    private final TokenBlockListService tokenBlockListService;

    public JwtBlocklistValidator(TokenBlockListService tokenBlockListService) {
        this.tokenBlockListService = tokenBlockListService;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        String jti = token.getId();
        if (tokenBlockListService.isTokenBlocked(jti)) {
            return OAuth2TokenValidatorResult.failure(
                    new OAuth2Error("invalid_token", "Token has been revoked", null)
            );
        }
        return OAuth2TokenValidatorResult.success();
    }
}