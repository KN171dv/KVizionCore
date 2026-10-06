# KVizionCore

Regras de negócio do sistema KVizion (vendas e orçamentos), separadas das telas Java Swing
para serem reaproveitadas no sistema web.

## Estrutura

- `model` – classes do domínio (Cliente, Produto, Pedido, Venda, Orcamento, ItemPedido, Usuario)
- `dao` – acesso ao banco MySQL com JDBC
- `service` – validações e regras de negócio
- `app` – `Main` com testes simples no console
- `test` – testes unitários com JUnit 4 (não precisam do banco)

## Como rodar

1. Criar o banco `kvizion` no MySQL (script `kvizionDB_v3.sql` do projeto desktop).
2. Copiar `config.exemplo.properties` para `config.properties` e colocar a senha do MySQL.
3. Abrir o projeto no NetBeans e executar (F6). O `Main` roda os testes e mostra o resultado no console.

## Testes unitários

Os testes ficam na pasta `test` (Test Packages no NetBeans). Para rodar: botão direito no projeto → **Test** (Alt+F6).
Eles testam cálculos e validações que não dependem do MySQL.
