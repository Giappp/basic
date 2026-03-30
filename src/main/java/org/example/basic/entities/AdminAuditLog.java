package org.example.basic.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin_audit_logs", indexes = {
        @Index(name = "idx_audit_admin", columnList = "admin_id"),
        @Index(name = "idx_audit_action", columnList = "action"),
        @Index(name = "idx_audit_created", columnList = "created_at")
})
@Getter
@NoArgsConstructor
public class AdminAuditLog extends BaseEntity {
}
