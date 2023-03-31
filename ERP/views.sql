-- view v3

-- Código para criação de views (visualizações das tabelas físicas)
-- O código pode ser executado em qualqer ordem

CREATE OR REPLACE VIEW vw_apontamento 
AS SELECT
    apontamento.apt_id,
    apontamento.hora_inicio,
    apontamento.hora_fim,
    usuario.nome as usuario_nome,
    apontamento.projeto,
    apontamento.cliente,
    apontamento.atividade,
    apontamento.justificativa
    FROM apontamento
    -- fazendo join com as tabelas usuário, projeto e cliente.
    JOIN usuario ON apontamento.usr_id = usuario.usr_id;

-- usuário
CREATE OR REPLACE VIEW vw_usuario 
AS SELECT
    usuario.usr_id,
    usuario.nome,
    usuario.perfil,
    usuario.matricula,
    usuario.salario,
    centro_resultado.nome as nome_cr
    FROM usuario
    -- fazendo join com a tabela de centro de resultado
    JOIN centro_resultado ON usuario.cr_id = centro_resultado.cr_id;

-- aprovação gestor
CREATE OR REPLACE VIEW vw_aprovacao
AS SELECT
    aprovacao.apv_id,
    apontamento.apt_id,
    usuario.usr_id as nome_gestor,
    aprovacao.data_verificacao,
    -- condição para trocar [v] ou [] por 'Aprovado' e 'Reprovado'
    CASE WHEN aprovacao.aprovado THEN 'Aprovado'
    ELSE 'Reprovado' END AS aprovacao

    FROM aprovacao
    JOIN apontamento ON aprovacao.apt_id = apontamento.apt_id
    JOIN usuario ON aprovacao.usr_id = usuario.usr_id;

CREATE OR REPLACE VIEW vw_centro_resultado
AS SELECT
    centro_resultado.cr_id,
    centro_resultado.nome
    FROM centro_resultado;