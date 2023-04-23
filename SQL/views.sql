-- view v3.5

-- Código para criação de views (visualizações das tabelas físicas)
-- O código pode ser executado em qualqer ordem

CREATE OR REPLACE VIEW public.vw_apontamento 
AS SELECT
    apontamento.apt_id,
    apontamento.hora_inicio,
    apontamento.hora_fim,
    apontamento.requester,
    -- usuario.nome as usuario_nome,
    apontamento.projeto,
    apontamento.cliente,
    apontamento.tipo,
    apontamento.justificativa,
    apontamento.cr_id,
    apontamento.aprovacao,
    apontamento.feedback 
    -- CASE 
    --     WHEN apontamento.aprovacao = 0 THEN 'Pendente'
    --     WHEN apontamento.aprovacao = 1 THEN 'Aprovado'
    --     ELSE 'Reproved' END 
    -- AS aprovacao

    FROM apontamento;
    -- fazendo join com as tabelas usuário, projeto e cliente.
    -- JOIN usuario ON apontamento.requester = usuario.nome;

-- usuário
CREATE OR REPLACE VIEW public.vw_usuario 
AS SELECT
    usuario.usr_id,
    usuario.nome,
    usuario.email,
    usuario.senha,
    usuario.tipo,
    usuario.matricula
    FROM usuario;