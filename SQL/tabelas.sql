-- versão 3.0

-- Código para criação de tabelas do Diagrama ERP
-- O código pode ser executado em qualqer ordem

-- apontamento
CREATE TABLE IF NOT EXISTS apontamento(
    apt_id serial NOT NULL,
    hora_inicio TIME null,
    hora_fim TIME null,
    usr_id INT NULL,
    projeto VARCHAR NULL,
    cliente VARCHAR NULL,
    tipo VARCHAR NULL,
    justificativa VARCHAR NULL,
    cr_id INT NULL,
    
    CONSTRAINT apontamento_pkey PRIMARY KEY (apt_id)
);

-- usuário
CREATE TABLE IF NOT EXISTS usuario(
    usr_id serial NOT NULL,
    nome VARCHAR NULL,
    email VARCHAR NULL,
    senha VARCHAR NULL,
    tipo VARCHAR NULL,
    matricula VARCHAR NULL,
    verba NUMERIC NULL,
    
    CONSTRAINT usuario_pkey PRIMARY KEY (usr_id)
);

-- aprovação do gestor
CREATE TABLE IF NOT EXISTS avaliacao(
    apv_id serial NOT NULL,
    apt_id INT NULL,
    usr_id INT NULL, -- usuário do gestor
    aprovado BOOLEAN DEFAULT FALSE,
    data_verificacao date DEFAULT now(),

    CONSTRAINT avaliacao_pkey PRIMARY KEY (apv_id)
);

-- centro de resultado
CREATE TABLE IF NOT EXISTS centro_resultado(
    cr_id serial NOT NULL,
    nome VARCHAR NULL,

    CONSTRAINT centro_resultado_pkey PRIMARY KEY (cr_id)
);