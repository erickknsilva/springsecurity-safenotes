package tech.buildrun.safenotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tech.buildrun.safenotes.entity.RefreshToken;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByOpaqueHash(String opaqueHash);

    @Modifying
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Query("UPDATE RefreshToken r SET r.revokedAt = :revokedAt WHERE r.familyId = :familyId AND r.revokedAt IS NULL")
    void updateRevokedAtByFamilyId(UUID familyId, Instant revokedAt);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying
    @Query("UPDATE RefreshToken r SET r.revokedAt = :revokedAt  where r.opaqueHash = :opaqueHash AND r.revokedAt IS NULL")
    int revokedIfNotRevoked(Instant revokedAt, String opaqueHash);

    boolean existsByOpaqueHashAndUserId(String opaqueHash, long userId);

    @Modifying
    @Query("UPDATE RefreshToken r SET r.revokedAt = :revokedAt where r.user.id = :userId AND r.revokedAt IS NULL")
    void revokeAllFromUserId(Instant revokedAt, long userId);
}
