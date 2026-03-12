package org.example.basic.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "tbl_refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @Column(nullable = false, unique = true)
    private UUID token;
    private LocalDateTime expiryDate;
    private String ipv4Address;
    private String deviceInfo;

    public boolean isExpire() {
        return expiryDate.isBefore(LocalDateTime.now());
    }
}
