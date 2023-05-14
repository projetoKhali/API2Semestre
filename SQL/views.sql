-- view v3.5

-- Código para criação de views (visualizações das tabelas físicas)
-- O código pode ser executado em qualqer ordem

CREATE OR REPLACE VIEW public.vw_apontamento 
AS SELECT
    apontamento.id,
    apontamento.hora_inicio,
    apontamento.hora_fim,
    apontamento.usr_id,
    usuario.nome as usuario_nome,
    apontamento.projeto,
    apontamento.cliente,
    apontamento.tipo,
    apontamento.justificativa,
    apontamento.cr_id,
    centro_resultado.nome as centro_nome,
    apontamento.aprovacao,
    apontamento.feedback

    FROM apontamento

    -- fazendo join com as tabelas usuário, projeto e cliente.
    JOIN usuario ON apontamento.usr_id = usuario.id 
    JOIN centro_resultado ON apontamento.cr_id = centro_resultado.id;


-- usuário
CREATE OR REPLACE VIEW public.vw_usuario 
AS SELECT
    usuario.id,
    usuario.nome,
    usuario.email,
    usuario.senha,
    usuario.tipo,
    usuario.matricula
    FROM usuario;


CREATE OR REPLACE VIEW public.vw_centro_resultado 
AS SELECT
    centro_resultado.id,
    centro_resultado.usr_id,
    centro_resultado.nome,
    centro_resultado.sigla,
    centro_resultado.codigo,
    usuario.nome as gestor_nome

    FROM centro_resultado

    -- fazendo join com a tabela usuário.
    JOIN usuario ON centro_resultado.usr_id = usuario.id;
