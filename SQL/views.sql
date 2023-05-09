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

    -- fazendo join com as tabelas usuário, projeto e cliente.
    JOIN usuario ON apontamento.usr_id = usuario.id;
    JOIN usuario ON apontamento.cr = centro_resultado.id;

    FROM apontamento;

-- usuário
CREATE OR REPLACE VIEW public.vw_usuario 
AS SELECT
    usuario.requester,
    usuario.nome,
    usuario.email,
    usuario.senha,
    usuario.tipo,
    usuario.matricula
    FROM usuario;