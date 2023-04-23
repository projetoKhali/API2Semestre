-- tabelas v3.5

-- Código para criação de tabelas do Diagrama ERP
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
    verba INT null,
    
    CONSTRAINT apontamento_pkey PRIMARY KEY (apt_id)
);

-- usuário
CREATE TABLE IF NOT EXISTS public.usuario(
    usr_id serial NOT NULL,
    nome VARCHAR NULL,
    email VARCHAR NULL,
    senha VARCHAR NULL,
    tipo VARCHAR NULL,
    matricula VARCHAR NULL,
    
    CONSTRAINT usuario_pkey PRIMARY KEY (usr_id)
);

-- centro de resultado
CREATE TABLE IF NOT EXISTS public.centro_resultado(
    cr_id serial NOT NULL,
    nome VARCHAR NULL,
    codigo VARCHAR NULL,
    sigla VARCHAR NULL,
    usr_id INT NULL,

    CONSTRAINT centro_resultado_pkey PRIMARY KEY (cr_id)
);

-- cliente
CREATE TABLE IF NOT EXISTS public.cliente(
    cli_id serial NOT NULL,
    nome VARCHAR NULL,
    cnpj VARCHAR NULL,

    CONSTRAINT  cliente_pkey PRIMARY KEY (cli_id)
);

-- pertence
CREATE  TABLE IF NOT EXISTS public.pertence(
    ptc_id serial NOT NULL,
    usr_id INT NULL,
    cr_id INT NULL,

    CONSTRAINT pertence_pkey PRIMARY KEY (ptc_id)
);