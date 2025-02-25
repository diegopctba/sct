CREATE TABLE IF NOT EXISTS usuarios
(
    id           uuid DEFAULT gen_random_uuid() NOT NULL,
    nome_usuario VARCHAR(255),
    senha        VARCHAR(255),
    CONSTRAINT usuarios_pk PRIMARY KEY (id)
);

INSERT INTO usuarios (nome_usuario, senha)
VALUES ('username', '$2a$10$b.QciyebIOTjPHo/72rE8.dVhjNHH0GnOCYw45CuWz6G2T.6X2uAK');
