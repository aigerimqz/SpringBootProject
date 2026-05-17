CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS users (
     id            UUID PRIMARY KEY,
     email         VARCHAR(255) NOT NULL UNIQUE,
     password_hash VARCHAR(255) NOT NULL,
     name          VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role    VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role)
);


WITH inserted_user AS (
    INSERT INTO users (id, email, password_hash, name)
        VALUES (
                   gen_random_uuid(),
                   'admin@gmail.com',
                   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHwy',
                   'Registry Admin'
               )
        ON CONFLICT (email) DO UPDATE SET email = EXCLUDED.email
        RETURNING id
)

INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_ADMIN'
FROM inserted_user
ON CONFLICT DO NOTHING;