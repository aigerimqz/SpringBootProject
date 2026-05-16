CREATE TABLE IF NOT EXISTS users (
    id            UUID PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    name          VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id UUID        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role    VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role)
);

-- Seeded admin: login with admin@cursed.kz / admin1234
INSERT INTO users (id, email, password_hash, name)
VALUES (
    gen_random_uuid(),
    'admin@cursed.kz',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lHwy',
    'Registry Admin'
    ) ON CONFLICT (email) DO NOTHING;

INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_ADMIN' FROM users WHERE email = 'admin@cursed.kz'
ON CONFLICT DO NOTHING;