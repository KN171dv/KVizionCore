# KVizionCore

Regras de negócio do sistema KVizion (vendas e orçamentos), separadas das telas Java Swing
para serem reaproveitadas no sistema web.

## Estrutura

- `model` – classes do domínio (Cliente, Produto, Pedido, Venda, Orcamento, ItemPedido, Usuario)
- `dao` – acesso ao banco MySQL com JDBC
- `service` – validações e regras de negócio
- `app` – `Main` com testes simples no console

## Como rodar

1. Criar o banco `kvizion` no MySQL (script `kvizionDB_v3.sql` do projeto desktop).
2. Copiar `config.exemplo.properties` para `config.properties` e colocar a senha do MySQL.
3. Abrir o projeto no NetBeans e executar (F6). O `Main` roda os testes e mostra o resultado no console.
