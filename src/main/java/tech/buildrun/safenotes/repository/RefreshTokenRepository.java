package tech.buildrun.safenotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.buildrun.safenotes.entity.RefreshToken;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
}
