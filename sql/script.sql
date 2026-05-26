-- =====================================================================
-- FinanceApp - Script de criacao do schema (PostgreSQL / Supabase)
--
-- COMO USAR NO SUPABASE:
--  1. Acesse https://supabase.com/ e crie um projeto.
--  2. No painel do projeto, va em "SQL Editor" > "New query".
--  3. Cole o conteudo deste arquivo e clique em "Run".
--
-- O Supabase ja fornece o banco "postgres" criado, por isso este script
-- nao executa CREATE DATABASE - ele cria apenas as tabelas e os dados
-- iniciais dentro do schema "public".
-- =====================================================================

DROP TABLE IF EXISTS movimento;
DROP TABLE IF EXISTS tipo_movimento;

-- ---------------------------------------------------------------------
-- Tabela com os tipos de movimento (categorias pre-cadastradas)
-- ---------------------------------------------------------------------
CREATE TABLE tipo_movimento (
    id        SERIAL       PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL UNIQUE,
    natureza  CHAR(1)      NOT NULL DEFAULT 'D'  -- 'D' = Despesa, 'R' = Receita
);

-- ---------------------------------------------------------------------
-- Tabela de lancamentos (movimentos financeiros)
-- ---------------------------------------------------------------------
CREATE TABLE movimento (
    id                  SERIAL        PRIMARY KEY,
    data_movimento      DATE          NOT NULL,
    tipo_movimento_id   INTEGER       NOT NULL,
    descricao           VARCHAR(255)  NOT NULL,
    valor               NUMERIC(12,2) NOT NULL,
    forma_pagamento     VARCHAR(20)   NOT NULL,  -- DINHEIRO, CARTAO, PIX
    banco               VARCHAR(60),             -- obrigatorio para CARTAO/PIX
    quantidade_parcelas INTEGER       NOT NULL DEFAULT 1,
    data_vencimento     DATE          NOT NULL,
    debitado            BOOLEAN       NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_movimento_tipo
        FOREIGN KEY (tipo_movimento_id) REFERENCES tipo_movimento(id)
);

CREATE INDEX idx_movimento_data ON movimento (data_movimento);
CREATE INDEX idx_movimento_venc ON movimento (data_vencimento);

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

-- ---------------------------------------------------------------------
-- Carga inicial de tipos de movimento
-- ---------------------------------------------------------------------
INSERT INTO tipo_movimento (descricao, natureza) VALUES
    ('Plano de Saude',    'D'),
    ('Salao',             'D'),
    ('Energia Eletrica',  'D'),
    ('Gas de Cozinha',    'D'),
    ('Celular',           'D'),
    ('Companhia de Agua', 'D'),
    ('Internet',          'D'),
    ('Supermercado',      'D'),
    ('Combustivel',       'D'),
    ('Salario',           'R'),
    ('Freelance',         'R'),
    ('Outras Despesas',   'D'),
    ('Outras Receitas',   'R');
