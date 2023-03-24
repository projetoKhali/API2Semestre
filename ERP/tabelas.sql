-- Código para criação de tabelas do Diagrama ERP
-- O código pode ser executado em qualqer ordem

-- apontamento
CREATE TABLE IF NOT EXISTS apontamento(
    apt_id serial NOT NULL,
    hora_inicio TIME null,
    hora_fim TIME null,
    usr_id INT NULL,
    prj_id INT NULL,
    clt_id INT NULL,
    justificativa VARCHAR NULL,
    
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

-- projeto
CREATE TABLE IF NOT EXISTS projeto(
    prj_id serial NOT NULL,
    clt_id INT NULL,
    nome VARCHAR NULL,

    CONSTRAINT projeto_pkey PRIMARY KEY (prj_id)
);

-- verificado
CREATE TABLE IF NOT EXISTS aprovacao_gestor(
    gst_apv_id serial NOT NULL,
    apt_id INT NULL,
    usr_id INT NULL,
    aprovado BOOLEAN DEFAULT FALSE,
    data_verificacao date DEFAULT now(),

    CONSTRAINT aprovacao_gestor_pkey PRIMARY KEY (vrf_id)
);

-- aprovado adm
CREATE TABLE IF NOT EXISTS aprovacao_adm(
    adm_apv_id serial NOT NULL,
    vrf_id INT NULL,
    aprovado_adm BOOLEAN DEFAULT NULL,

    CONSTRAINT aprovacao_adm_pkey PRIMARY KEY (adm_apv_id)
);

-- squad
CREATE TABLE IF NOT EXISTS squad(
    sqd_id serial NOT NULL,
    nome_squad VARCHAR NULL,

    CONSTRAINT squad_pkey PRIMARY KEY (sqd_id)
);

-- atividade
CREATE TABLE IF NOT EXISTS atividade(
    atv_id serial NOT NULL,
    descricao VARCHAR NULL,
    percentual NUMERIC NULL,

    CONSTRAINT atividade_pkey PRIMARY KEY (atv_id)
);

-- cliente
CREATE TABLE IF NOT EXISTS cliente(
    clt_id serial NOT NULL,
    nome VARCHAR NULL,

    CONSTRAINT cliente_pkey PRIMARY KEY (clt_id)
);