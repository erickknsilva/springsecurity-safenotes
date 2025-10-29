package tech.buildrun.safenotes.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import tech.buildrun.safenotes.config.OpaqueToken;
import tech.buildrun.safenotes.controller.dto.LogoutRequest;
import tech.buildrun.safenotes.repository.RefreshTokenRepository;

import java.time.Instant;

@Service
public class LogoutService {


    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlockListService tokenBlockListService;

    public LogoutService(RefreshTokenRepository refreshTokenRepository, TokenBlockListService tokenBlockListService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenBlockListService = tokenBlockListService;
    }


    public void logout(Jwt jwt, LogoutRequest refreshToken) {

        long userId = Long.parseLong(jwt.getSubject());
        String opaqueHash = OpaqueToken.generateOpaqueHash(refreshToken.refreshToken());

        var isRefreshFromUser = refreshTokenRepository.existsByOpaqueHashAndUserId(opaqueHash, userId);

        if(!isRefreshFromUser){
            throw new RuntimeException("Invalid refresh token for user");
        }

        refreshTokenRepository.revokedIfNotRevoked(Instant.now(), opaqueHash);
        tokenBlockListService.blockToken(jwt.getId(), jwt.getExpiresAt());
    }

}
