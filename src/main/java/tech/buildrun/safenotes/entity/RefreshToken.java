package tech.buildrun.safenotes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_refresh_tokens")
public class RefreshToken {

    @Id
    @Column(name = "jti", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID jti;

    @Column(name = "family_id", nullable = false)
    private UUID familyId;

    @Column(name = "opaque_hash", unique = true, nullable = false)
    private String opaqueHash;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

}
