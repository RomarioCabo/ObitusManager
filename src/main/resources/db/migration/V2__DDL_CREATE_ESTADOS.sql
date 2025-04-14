CREATE TABLE IF NOT EXISTS estados
(
    id_estado UUID         NOT NULL,
    nome      VARCHAR(255) NULL,
    sigla     VARCHAR(255) NOT NULL,
    ativo     BOOLEAN      NOT NULL,
    CONSTRAINT pk_id_estado PRIMARY KEY (id_estado)
);