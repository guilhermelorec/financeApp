-- =====================================================================
-- Migracao 001 - Separa "Outros" em "Outras Despesas" e "Outras Receitas"
--
-- Rode UMA UNICA VEZ no SQL Editor do Supabase.
-- Nao precisa recriar tabelas - mantem todos os movimentos existentes.
-- =====================================================================

-- 1) Renomeia o tipo "Outros" (Despesa) para "Outras Despesas"
UPDATE tipo_movimento
   SET descricao = 'Outras Despesas'
 WHERE descricao = 'Outros';

-- 2) Cria a contraparte para Receita (se ainda nao existir)
INSERT INTO tipo_movimento (descricao, natureza)
SELECT 'Outras Receitas', 'R'
 WHERE NOT EXISTS (
    SELECT 1 FROM tipo_movimento WHERE descricao = 'Outras Receitas'
 );
