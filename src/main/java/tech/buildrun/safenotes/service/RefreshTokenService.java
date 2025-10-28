package tech.buildrun.safenotes.service;

import com.nimbusds.jwt.JWT;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import tech.buildrun.safenotes.config.JwtConfig;
import tech.buildrun.safenotes.config.OpaqueToken;
import tech.buildrun.safenotes.entity.RefreshToken;
import tech.buildrun.safenotes.entity.User;
import tech.buildrun.safenotes.repository.RefreshTokenRepository;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {


    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtConfig jwtConfig;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtConfig jwtConfig) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtConfig = jwtConfig;
    }

    @Transactional
    public String generateRefreshToken(User user, UUID familyId) {
        //refresh token é do tipo opaque, ou seja, um valor aleatorio que nao carrega informacoes
        //precisa de um tipo especifico para garantir a unicidade do token
        var opaqueToken = OpaqueToken.generate(); //gerar o token opaco
        var opaqueHash = OpaqueToken.generateOpaqueHash(opaqueToken); //gerar o hash do token opaco para armazenar no banco

        refreshTokenRepository.save(new RefreshToken(familyId, opaqueHash, user, Instant.now(),
                Instant.now().plusSeconds(jwtConfig.getRefreshExpiresIn())));

        return opaqueToken;
    }
}
