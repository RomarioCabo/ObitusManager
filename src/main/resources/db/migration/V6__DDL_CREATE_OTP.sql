CREATE TABLE IF NOT EXISTS one_time_password
(
    id_otp      UUID         NOT NULL,
    codigo_hash VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    criado_em   TIMESTAMP    NOT NULL,
    usado       BOOLEAN      NOT NULL,
    tentativas  INTEGER      NOT NULL,
    CONSTRAINT pk_id_otp PRIMARY KEY (id_otp)
);