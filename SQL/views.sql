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
    apontamento.tipo,
    apontamento.justificativa,
    centro_resultado.nome as cr_nome
    FROM apontamento
    -- fazendo join com as tabelas usuário, projeto e cliente.
    JOIN usuario ON apontamento.usr_id = usuario.usr_id
    JOIN centro_resultado on apontamento.cr_id = centro_resultado.cr_id;

-- usuário
CREATE OR REPLACE VIEW vw_usuario 
AS SELECT
    usuario.usr_id,
    usuario.nome,
    usuario.email,
    usuario.senha,
    usuario.tipo,
    usuario.matricula,
    usuario.verba
    FROM usuario;

CREATE OR REPLACE VIEW vw_avaliacao
AS SELECT
    avaliacao.apv_id,
    apontamento.apt_id,
    usuario.usr_id as nome_gestor,
    avaliacao.data_verificacao,
    -- condição para trocar [v] ou [] por 'Aprovado' e 'Reprovado'
    CASE WHEN avaliacao.aprovado THEN 'Aprovado'
    ELSE 'Reprovado' END AS avaliacao

    FROM avaliacao
    JOIN apontamento ON avaliacao.apt_id = apontamento.apt_id
    JOIN usuario ON avaliacao.usr_id = usuario.usr_id;


CREATE OR REPLACE VIEW vw_centro_resultado
AS SELECT
    centro_resultado.cr_id,
    centro_resultado.nome
    FROM centro_resultado;