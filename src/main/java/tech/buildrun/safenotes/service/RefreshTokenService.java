package tech.buildrun.safenotes.service;

import com.nimbusds.jwt.JWT;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.buildrun.safenotes.config.JwtConfig;
import tech.buildrun.safenotes.config.OpaqueToken;
import tech.buildrun.safenotes.controller.dto.LoginResponse;
import tech.buildrun.safenotes.entity.RefreshToken;
import tech.buildrun.safenotes.entity.User;
import tech.buildrun.safenotes.repository.RefreshTokenRepository;
import tech.buildrun.safenotes.repository.UserRepository;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtConfig jwtConfig;
    private final AccessTokenService accessTokenService;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtConfig jwtConfig,
                               AccessTokenService accessTokenService, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtConfig = jwtConfig;
        this.accessTokenService = accessTokenService;
        this.userRepository = userRepository;
    }


    @Transactional
    public LoginResponse refreshToken(String opaqueToken) {
        // 1 - validar se o opaquetoken existe, através do hash que vai ser utilizado no banco de dados
        var opaqueHash = OpaqueToken.generateOpaqueHash(opaqueToken);
        var refreshToken = getRefreshToken(opaqueHash);

        // 2 - validar se ele está espirado (lançar exceção)
        isTokenExpired(refreshToken);

        // 3 - Validar se o refresh token ja foi utilizado (revogar todos os token da familia)
        if (refreshToken.getRevokedAt() != null) {
            logger.warn("Refresh token already revoked - security issue - revoking token family...");
            revokeTokenFamily(refreshToken.getFamilyId());
            throw new RuntimeException("Refresh token already revoked.");
        }

        //refresh token validado
        // 4 - marca como revogado
        refreshCurrentRefreshToken(refreshToken);
        var user = userRepository.getReferenceById(refreshToken.getUser().getId());


        // 5 - emitir um novo accessToken para o usuario autenticado
        var accessToken = accessTokenService.generateAccessToken(user, refreshToken.getFamilyId());

        // 6 - emitir um novo refresh token
        var newRefreshToken = this.generateRefreshToken(user, refreshToken.getFamilyId());

        // 7 - retornar os dados do login response
        return  new LoginResponse(
                accessToken.getTokenValue(),
                jwtConfig.getExpiresIn(),
                newRefreshToken,
                jwtConfig.getRefreshExpiresIn(),
                accessToken.getClaimAsStringList("scope"));

    }

    private void refreshCurrentRefreshToken(RefreshToken refreshToken) {

        int rowsUpdate = refreshTokenRepository.revokedIfNotRevoked(Instant.now(), refreshToken.getOpaqueHash());

        if (rowsUpdate == 0) {
            logger.warn("Refresh token already revoked - security issue - revoking token family...");
            revokeTokenFamily(refreshToken.getFamilyId());
            throw new RuntimeException("Refresh token already revoked");
        }
    }

    private void revokeTokenFamily(UUID familyId) {
        refreshTokenRepository.updateRevokedAtByFamilyId(familyId, Instant.now());
    }

    private static boolean isTokenExpired(RefreshToken refreshToken) {
        if (Instant.now().isAfter(refreshToken.getExpiresAt())) {
            throw new RuntimeException("Refresh token expired");
        }
        return false;
    }

    private RefreshToken getRefreshToken(String opaqueHash) {
        return refreshTokenRepository.findByOpaqueHash(opaqueHash)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
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
