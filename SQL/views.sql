-- view v3.5

-- Código para criação de views (visualizações das tabelas físicas)
-- O código pode ser executado em qualqer ordem

CREATE OR REPLACE VIEW public.vw_apontamento 
AS SELECT
    apontamento.apt_id,
    apontamento.hora_inicio,
    apontamento.hora_fim,
    apontamento.requester,
    apontamento.projeto,
    apontamento.cliente,
    apontamento.tipo,
    apontamento.justificativa,
    apontamento.cr_id as centro_resultado,
    CASE 
        -- verifica se apontamento foi aprovado ou reprovado
        WHEN apontamento.aprovacao = 0 THEN 'Pendente'
        WHEN apontamento.aprovacao = 1 THEN 'Aprovado'
        ELSE 'Reproved' END AS aprovacao

    FROM apontamento;

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

-- resultado
CREATE OR REPLACE VIEW public.vw_centro_resultado
AS SELECT
    centro_resultado.cr_id,
    centro_resultado.nome,
    centro_resultado.codigo,
    centro_resultado.sigla,
    centro_resultado.usr_id,
    usuario.nome as usuario_nome

    FROM centro_resultado
    left JOIN usuario on centro_resultado.usr_id = usuario.usr_id;

-- cliente
CREATE OR REPLACE VIEW public.vw_cliente
AS SELECT
    cliente.cli_id,
    cliente.nome,
    cliente.cnpj
    FROM cliente;

-- pertence
CREATE OR REPLACE VIEW public.vw_pertence
AS SELECT
    pertence.ptc_id,
    pertence.usr_id,
    pertence.cr_id
    FROM pertence;