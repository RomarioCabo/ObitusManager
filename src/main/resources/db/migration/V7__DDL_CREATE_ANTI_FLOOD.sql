CREATE TABLE IF NOT EXISTS anti_flood
(
    id_antiflood    UUID         NOT NULL,
    email           VARCHAR(255) NOT NULL,
    tentativas      INT          NOT NULL,
    inicio_bloqueio TIMESTAMP,
    fim_bloqueio    TIMESTAMP,
    nivel_bloqueio  INT          NOT NULL,
    CONSTRAINT pk_id_antiflood PRIMARY KEY (id_antiflood)
);
CREATE UNIQUE INDEX idx_anti_flood_unique ON anti_flood (email);