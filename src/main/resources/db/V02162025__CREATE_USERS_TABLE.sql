CREATE TABLE users (
    pid         BIGINT PRIMARY KEY,
    username    VARCHAR(255) UNIQUE,
    first_name  VARCHAR(255),
    last_name   VARCHAR(255),
    came_from   VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT now(),
    accessed_at TIMESTAMP    NOT NULL DEFAULT now()
);