package online.dummydev.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tenants")
public class TenantEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(
            name = "clerk_org_id",
            nullable = false,
            unique = true,
            length = 100
    )
    private String clerkOrgId;

    @Column(
            name = "org_name",
            nullable = false
    )
    private String orgName;
}
