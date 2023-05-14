-- tabelas v3.5

-- Código para criação de tabelas do Diagrama ERD
-- O código pode ser executado em qualqer ordem

-- apontamento
-- apontamento
CREATE TABLE IF NOT EXISTS public.apontamento(
    id serial NOT NULL,
    hora_inicio TIMESTAMP null,
    hora_fim TIMESTAMP null,
    usr_id INT NULL,
    projeto VARCHAR NULL,
    cliente VARCHAR NULL,
    tipo BOOLEAN NULL,
    justificativa VARCHAR NULL,
    cr_id int NULL,
    aprovacao INT DEFAULT 0,
    feedback VARCHAR NULL,
    
    CONSTRAINT apontamento_pkey PRIMARY KEY (id)
);

-- usuário
CREATE TABLE IF NOT EXISTS public.usuario(
    id serial NOT NULL,
    nome VARCHAR NULL,
    email VARCHAR NULL,
    senha VARCHAR NULL,
    tipo INT NOT NULL,
    matricula VARCHAR NULL,
    
    CONSTRAINT usuario_pkey PRIMARY KEY (id)
);

-- centro de resultado
CREATE TABLE IF NOT EXISTS public.centro_resultado(
    id serial NOT NULL,
    usr_id INT NOT NULL,
    nome VARCHAR NULL,
    sigla VARCHAR NULL,
    codigo VARCHAR NULL,


    CONSTRAINT centro_resultado_pkey PRIMARY KEY (id)
);

-- relação user <-> cr
CREATE TABLE IF NOT EXISTS public.membro_cr (
    id serial NOT NULL,
    usr_id INT NOT NULL,
    cr_id INT NOT NULL,

    CONSTRAINT membro_cr_pkey PRIMARY KEY (id)
);