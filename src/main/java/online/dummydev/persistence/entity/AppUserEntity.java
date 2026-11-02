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
@Table(name = "app_users")
public class AppUserEntity extends TenantScopedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(
            name = "role_id",
            nullable = false
    )
    private UUID roleId;

    @Column(
            name = "clerk_user_id",
            nullable = false,
            length = 100
    )
    private String clerkUserId;

    @Column(
            name = "first_name",
            length = 100
    )
    private String firstName;

    @Column(
            name = "last_name",
            length = 100
    )
    private String lastName;

    @Column(
            name = "email",
            nullable = false
    )
    private String email;
}
