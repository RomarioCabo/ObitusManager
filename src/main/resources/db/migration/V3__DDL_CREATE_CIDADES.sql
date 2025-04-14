CREATE TABLE IF NOT EXISTS cidades
(
    id_cidade   UUID         NOT NULL,
    id_estado   UUID         NOT NULL,
    nome_cidade VARCHAR(255) NOT NULL,
    CONSTRAINT pk_id_cidade PRIMARY KEY (id_cidade),
    CONSTRAINT fk_id_estado FOREIGN KEY (id_estado) REFERENCES estados (id_estado)
);