-- Código para criação de views (visualizações das tabelas físicas)
-- O código pode ser executado em qualqer ordem

CREATE OR REPLACE VIEW vw_apontamento 
AS SELECT
    apontamento.apt_id,
    apontamento.hora_inicio,
    apontamento.hora_fim,
    usuario.nome as usuario_nome,
    projeto.nome as projeto_nome,
    apontamento.justificativa,
    FROM apontamento
    -- fazendo join com as tabelas usuário, projeto e cliente.
    JOIN usuario ON apontamento.usr_id = usuario.usr_id,
    JOIN projeto ON apontamento.prj_id = projeto.prj_id;

-- usuário
CREATE OR REPLACE VIEW vw_usuario 
AS SELECT
    usuario.usr_id,
    usuario.nome,
    usuario.perfil,
    usuario.matricula,
    usuario.salario,
    squad.nome,
    FROM usuario
    -- fazendo join com a tabela de squad's
    JOIN squad ON usuario.sqd_id = squad.sqd_id;

-- projeto
CREATE OR REPLACE VIEW vw_projeto
AS SELECT
    projeto.prj_id,
    projeto.nome,
    cliente.nome as cliente_nome,
    from projeto
    JOIN cliente on projeto.clt_id = cliente.clt_id;

-- aprovação gestor
CREATE OR REPLACE VIEW vw_aprovacao_gst
AS SELECT
    aprovacao_gst.gst_apv_id,
    apontamento.apt_id,
    usuario.usr_id as nome_gestor,
    aprovacao_gst.data_verificacao,
    -- condição para trocar [v] ou [] por 'Aprovado' e 'Reprovado'
    CASE WHEN aprovacao_gst.aprovado_gst THEN 'Aprovado'
    ELSE 'Reprovado' END AS aprovacao_gestor

    FROM aprovacao_gst
    JOIN apontamento ON aprovacao_gst.apt_id = apontamento.apt_id
    JOIN usuario ON aprovacao_gst.usr_id = usuario.usr_id;

-- aprovado adm
CREATE OR REPLACE VIEW vw_aprovacao_adm
AS SELECT
    aprovacao_adm.adm_apv_id,
    usuario.usr_id as nome_adm,
    
    -- aprovação gestor
    CASE WHEN aprovacao_gst.aprovado_gst THEN 'Aprovado'
    ELSE 'Reprovado' END AS aprovacao_gestor,

    -- aprovação adm
    CASE aprovacao_adm.aprovado_adm THEN 'Aprovado'
    ELSE 'Reprovado' END AS aprovacao_administrador

    FROM aprovacao_adm
    JOIN aprovacao_gst ON aprovacao_adm.gst_apv_id = aprovacao_gst.gst_apv_id
    JOIN usuario ON aprovacao_adm.usr_id = usuario.usr_id;