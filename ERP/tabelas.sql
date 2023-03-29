-- Código para criação de tabelas do Diagrama ERP
-- O código pode ser executado em qualqer ordem

-- apontamento
CREATE TABLE IF NOT EXISTS apontamento(
    apt_id serial NOT NULL,
    hora_inicio TIME null,
    hora_fim TIME null,
    justificativa VARCHAR NULL,
    projeto VARCHAR NULL,
    cliente VARCHAR NULL,
    
    CONSTRAINT apontamento_pkey PRIMARY KEY (apt_id)
);

-- usuário
CREATE TABLE IF NOT EXISTS usuario(
    usr_id serial NOT NULL,
    nome VARCHAR NULL,
    perfil VARCHAR NULL,
    matricula VARCHAR NULL,
    salario NUMERIC NULL,
    sqd_id INT NULL,
    
    CONSTRAINT usuario_pkey PRIMARY KEY (usr_id)
);

-- aprovação do gestor
CREATE TABLE IF NOT EXISTS aprovacao_gestor(
    gst_apv_id serial NOT NULL,
    apt_id INT NULL,
    usr_id INT NULL, -- usuário do gestor
    aprovado_gst BOOLEAN DEFAULT FALSE,
    data_verificacao date DEFAULT now(),

    CONSTRAINT aprovacao_gestor_pkey PRIMARY KEY (gst_apv_id)
);

-- aprovação do adm
CREATE TABLE IF NOT EXISTS aprovacao_adm(
    adm_apv_id serial NOT NULL,
    gst_apv_id INT NULL,
    usr_id INT NULL, -- usuário do adm
    aprovado_adm BOOLEAN DEFAULT NULL,

    CONSTRAINT aprovacao_adm_pkey PRIMARY KEY (adm_apv_id)
);

-- squad
CREATE TABLE IF NOT EXISTS squad(
    sqd_id serial NOT NULL,
    nome VARCHAR NULL,

    CONSTRAINT squad_pkey PRIMARY KEY (sqd_id)
);

-- atividade
CREATE TABLE IF NOT EXISTS atividade(
    atv_id serial NOT NULL,
    descricao VARCHAR NULL,
    percentual NUMERIC NULL,

    CONSTRAINT atividade_pkey PRIMARY KEY (atv_id)
);