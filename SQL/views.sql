-- view v3.5

-- Código para criação de views (visualizações das tabelas físicas)
-- O código pode ser executado em qualqer ordem

CREATE OR REPLACE VIEW vw_apontamento 
AS SELECT
    apontamento.apt_id,
    apontamento.hora_inicio,
    apontamento.hora_fim,
    apontamento.requester,
    usuario.nome as usuario_nome,
    apontamento.projeto,
    apontamento.cliente,
    apontamento.tipo,
    apontamento.justificativa,
    apontamento.cr_id,
    centro_resultado.nome as cr_nome
    FROM apontamento
    -- fazendo join com as tabelas usuário, projeto e cliente.
    JOIN usuario ON apontamento.requester = usuario.nome
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
-- INSERT INTO apontamento (hora_inicio,hora_fim,usr_id,projeto,cliente,tipo,justificativa,cr_id)
-- VALUES ('12:12:12','13:13:13',1,'api','2rp','hora extra','nota',1);

CREATE OR REPLACE VIEW vw_avaliacao
AS SELECT
    avaliacao.apv_id,
    apontamento.apt_id,
    avaliacao.usr_id,
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