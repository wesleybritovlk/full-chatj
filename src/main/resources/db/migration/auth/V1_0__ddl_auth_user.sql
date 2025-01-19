CREATE TABLE IF NOT EXISTS auth.tb_auth_user (
    id UUID PRIMARY KEY DEFAULT public.uuid7(),
    avatar_url VARCHAR,
    email VARCHAR,
    password VARCHAR,
    full_name VARCHAR,
    birth_date DATE,
    platforms UUID[],
    providers UUID[],
    scopes VARCHAR[],
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMPTZ
);