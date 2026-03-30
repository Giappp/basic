package org.example.basic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admin_accounts", indexes = {
        @Index(name = "idx_admin_username", columnList = "username"),
        @Index(name = "idx_admin_email", columnList = "email"),
        @Index(name = "idx_admin_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
public class AdminAccount extends BaseEntity {
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdminRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdminStatus status = AdminStatus.ACTIVE;

    @Column(name = "force_password_change", nullable = false)
    private boolean forcePasswordChange = true;

    @Column(name = "password_changed_at")
    private Instant passwordChangedAt;

    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts = 0;

    @Column(name = "locked_until")
    private Instant lockedUntil;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "last_login_ip", length = 45)
    private String lastLoginIp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private AdminAccount createdBy;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdminSession> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<AdminAuditLog> auditLogs = new ArrayList<>();

    public boolean isLocked() {
        return lockedUntil != null && lockedUntil.isAfter(Instant.now());
    }

    public boolean isActive() {
        return status == AdminStatus.ACTIVE;
    }

    public boolean isPasswordExpired(long maxAgeDays) {
        if (passwordChangedAt == null) return true;
        return passwordChangedAt.plusSeconds(maxAgeDays).isBefore(Instant.now());
    }
}
