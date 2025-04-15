CREATE TABLE IF NOT EXISTS notas_falecimento
(
    id_nota_falecimento         UUID         NOT NULL,
    id_cidade                   UUID         NOT NULL,
    nome_falecido               VARCHAR(255) NOT NULL,
    idade                       INTEGER      NOT NULL,
    data_falecimento            DATE         NOT NULL,
    local_velorio               VARCHAR(255) NOT NULL,
    local_sepultamento          VARCHAR(255) NOT NULL,
    data_hora_sepultamento      TIMESTAMP    NOT NULL,
    biografia_resumida_falecido TEXT,
    foto                        OID,
    CONSTRAINT pk_id_nota_falecimento PRIMARY KEY (id_nota_falecimento),
    CONSTRAINT fk_id_cidade FOREIGN KEY (id_cidade) REFERENCES cidades (id_cidade)
);