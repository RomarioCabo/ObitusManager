CREATE TABLE IF NOT EXISTS usuarios
(
    id_usuario      UUID         NOT NULL,
    nome            VARCHAR(255) NULL,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password        VARCHAR(255) NULL,
    role            VARCHAR(255) NOT NULL,
    created_at      TIMESTAMP    NOT NULL,
    updated_at      TIMESTAMP    NULL DEFAULT NULL,
    CONSTRAINT pk_id_usuario PRIMARY KEY (id_usuario)
);
CREATE UNIQUE INDEX idx_usuarios_email_unique ON usuarios (email);