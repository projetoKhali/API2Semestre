-- tabelas v3.5

-- Código para criação de tabelas do Diagrama ERD
-- O código pode ser executado em qualqer ordem

-- apontamento
-- apontamento
CREATE TABLE IF NOT EXISTS public.apontamento(
    apt_id serial NOT NULL,
    hora_inicio TIMESTAMP null,
    hora_fim TIMESTAMP null,
    requester VARCHAR NULL,
    projeto VARCHAR NULL,
    cliente VARCHAR NULL,
    tipo BOOLEAN NULL,
    justificativa VARCHAR NULL,
    cr_id VARCHAR NULL,
    aprovacao INT DEFAULT 0,
    feedback VARCHAR NULL,
    
    CONSTRAINT apontamento_pkey PRIMARY KEY (apt_id)
);

-- usuário
CREATE TABLE IF NOT EXISTS public.usuario(
    requester serial NOT NULL,
    nome VARCHAR NULL,
    email VARCHAR NULL,
    senha VARCHAR NULL,
    tipo VARCHAR NULL,
    matricula VARCHAR NULL,
    
    CONSTRAINT usuario_pkey PRIMARY KEY (requester)
);

-- centro de resultado
CREATE TABLE IF NOT EXISTS public.centro_resultado(
    cr_id serial NOT NULL,
    nome VARCHAR NULL,

    CONSTRAINT centro_resultado_pkey PRIMARY KEY (cr_id)
);