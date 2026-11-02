package online.dummydev.persistence.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.TenantId;

import java.util.UUID;

@Getter
@MappedSuperclass
public abstract class TenantScopedEntity
        extends AuditableEntity {

    @TenantId
    private UUID tenantId;
}
