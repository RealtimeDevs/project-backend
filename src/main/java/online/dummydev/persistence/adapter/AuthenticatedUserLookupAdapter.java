package online.dummydev.persistence.adapter;

import lombok.RequiredArgsConstructor;
import online.dummydev.application.contracts.persistence.AuthenticatedUserLookup;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthenticatedUserLookupAdapter
        implements AuthenticatedUserLookup {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public AuthenticatedUserData resolve(
            String clerkUserId,
            String clerkOrgId) {

        String sql = """
            SELECT
                t.id AS tenant_id,
                u.id AS app_user_id,
                r.id AS role_id,
                r.code AS role_code
            FROM tenants t
            INNER JOIN app_users u
                ON u.tenant_id = t.id
            INNER JOIN roles r
                ON r.id = u.role_id
            WHERE t.clerk_org_id = ?
              AND u.clerk_user_id = ?
              AND t.deleted_at IS NULL
              AND u.deleted_at IS NULL
              AND r.deleted_at IS NULL
            """;

        var results = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new online.dummydev.application.contracts.persistence.AuthenticatedUserLookup.AuthenticatedUserData(
                        rs.getObject("tenant_id", java.util.UUID.class),
                        rs.getObject("app_user_id", java.util.UUID.class),
                        rs.getObject("role_id", java.util.UUID.class),
                        rs.getString("role_code")
                ),
                clerkOrgId,
                clerkUserId
        );

        if (results.size() != 1) {
            throw new IllegalStateException(
                    "Active application membership not found"
            );
        }

        return results.getFirst();
    }
}