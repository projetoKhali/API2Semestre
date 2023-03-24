-- Código para criação de views (visualizações das tabelas físicas)
-- O código pode ser executado em qualqer ordem

CREATE OR REPLACE VIEW vw_apontamento 
AS SELECT
    apontamento.apt_id,
    apontamento.hora_inicio,
    apontamento.hora_fim,
    usuario.nome as usuario_nome,
    projeto.nome as projeto_nome,
    apontamento.justificativa
    FROM apontamento
    -- fazendo join com as tabelas usuário, projeto e cliente.
    JOIN usuario ON apontamento.usr_id = usuario.usr_id
    JOIN projeto ON apontamento.prj_id = projeto.prj_id;

-- usuário
CREATE OR REPLACE VIEW vw_usuario AS SELECT(
    usuario.usr_id,
    usuario.nome,
    usuario.perfil,
    usuario.matricula,
    usuario.salario,
    squad.nome,
    FROM usuario
    -- fazendo join com a tabela de squad's
    JOIN squad ON usuario.sqd_id = squad.sqd_id;
);