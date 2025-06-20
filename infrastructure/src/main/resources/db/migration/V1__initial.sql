CREATE TABLE category (
    id VARCHAR(36) PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP(9) NOT NULL,
    updated_at TIMESTAMP(9) NOT NULL,
    deleted_at TIMESTAMP(9) NULL
);
