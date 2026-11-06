
-- ==========================================
-- 1. TENANTS TABLE
-- ==========================================

CREATE TABLE tenants (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                         clerk_org_id VARCHAR(100) NOT NULL UNIQUE,
                         org_name VARCHAR(255) NOT NULL,

                         version BIGINT NOT NULL DEFAULT 0,

                         created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                         created_by UUID,
                         updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                         updated_by UUID,
                         deleted_at TIMESTAMPTZ,
                         deleted_by UUID
);

-- ==========================================
-- 2. ROLES TABLE
-- ==========================================

CREATE TABLE roles (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                       code VARCHAR(50) NOT NULL UNIQUE,
                       name VARCHAR(100) NOT NULL,
                       description VARCHAR(255),

                       version BIGINT NOT NULL DEFAULT 0,

                       created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                       created_by UUID,
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                       updated_by UUID,
                       deleted_at TIMESTAMPTZ,
                       deleted_by UUID
);

-- ==========================================
-- 3. SEED APPLICATION ROLES
-- ==========================================

INSERT INTO roles (code, name, description)
VALUES
    ('ADMIN', 'Admin', 'Organization administrator'),
    ('MANAGER', 'Manager', 'Manages tickets and staff'),
    ('IT', 'IT', 'Handles IT support tickets'),
    ('HR', 'HR', 'Handles HR support tickets'),
    ('STAFF', 'Staff', 'Creates and tracks tickets');

-- ==========================================
-- 4. APP USERS TABLE
-- ==========================================

CREATE TABLE app_users (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                           tenant_id UUID NOT NULL,
                           role_id UUID NOT NULL,
                           clerk_user_id VARCHAR(100) NOT NULL,

                           first_name VARCHAR(100),
                           last_name VARCHAR(100),
                           email VARCHAR(255) NOT NULL,

                           version BIGINT NOT NULL DEFAULT 0,

                           created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                           created_by UUID,
                           updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                           updated_by UUID,
                           deleted_at TIMESTAMPTZ,
                           deleted_by UUID,

                           CONSTRAINT fk_app_users_tenant
                               FOREIGN KEY (tenant_id)
                                   REFERENCES tenants(id),

                           CONSTRAINT fk_app_users_role
                               FOREIGN KEY (role_id)
                                   REFERENCES roles(id),

                           CONSTRAINT uq_app_users_tenant_clerk_user
                               UNIQUE (tenant_id, clerk_user_id)
);
