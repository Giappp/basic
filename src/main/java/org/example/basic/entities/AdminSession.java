package org.example.basic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "admin_sessions", indexes = {
        @Index(name = "idx_session_token", columnList = "token"),
        @Index(name = "idx_session_admin", columnList = "admin_id")
})
@Getter
@Setter
@NoArgsConstructor
public class AdminSession extends BaseEntity {

    @Column(nullable = false, unique = true, length = 64)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private AdminAccount admin;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;
}
