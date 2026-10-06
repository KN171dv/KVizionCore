package app;

import java.util.List;
import model.Cliente;
import model.Orcamento;
import model.Produto;
import model.Usuario;
import model.Venda;
import service.ClienteService;
import service.LoginService;
import service.OrcamentoService;
import service.ProdutoService;
import service.RegraNegocioException;
import service.VendaService;

// Testes simples das regras de negocio, sem nenhuma tela.
// Precisa do MySQL ligado e do banco kvizion criado.
// Tudo que o teste cadastra ele apaga no final.
public class Main {

    private static int testesOk = 0;
    private static int testesComErro = 0;

    public static void main(String[] args) {
        try {
            testarLogin();
            testarCliente();
            testarProduto();
            testarVenda();
            testarOrcamento();
            limparDadosDeTeste();
        } catch (Exception e) {
            System.out.println("ERRO INESPERADO: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("Testes OK: " + testesOk + " | Com erro: " + testesComErro);
    }

    private static void conferir(String descricao, boolean passou) {
        if (passou) {
            testesOk++;
            System.out.println("[OK]     " + descricao);
        } else {
            testesComErro++;
            System.out.println("[FALHOU] " + descricao);
        }
    }

    private static void testarLogin() throws Exception {
        System.out.println("--- Login ---");
        LoginService loginService = new LoginService();

        Usuario admin = loginService.autenticar("admin", "admin123");
        conferir("login do admin funciona", admin.isAdmin());

        try {
            loginService.autenticar("admin", "senhaerrada");
            conferir("senha errada e recusada", false);
        } catch (RegraNegocioException e) {
            conferir("senha errada e recusada (" + e.getMessage() + ")", true);
        }
    }

    private static void testarCliente() throws Exception {
        System.out.println("--- Cliente ---");
        ClienteService clienteService = new ClienteService();

        try {
            clienteService.cadastrar(new Cliente(0, "", "Rua X", "99999-0000"));
            conferir("cliente sem nome e recusado", false);
        } catch (RegraNegocioException e) {
            conferir("cliente sem nome e recusado (" + e.getMessage() + ")", true);
        }

        try {
            clienteService.cadastrar(new Cliente(0, "Cliente Teste", "Rua X", "abc"));
            conferir("telefone invalido e recusado", false);
        } catch (RegraNegocioException e) {
            conferir("telefone invalido e recusado (" + e.getMessage() + ")", true);
        }

        clienteService.cadastrar(new Cliente(0, "Cliente Teste", "Rua X", "(21) 99999-0000"));
        List<Cliente> achados = clienteService.pesquisar("Cliente Teste");
        conferir("cliente cadastrado e encontrado na pesquisa", achados.size() == 1);

        Cliente salvo = achados.get(0);
        clienteService.atualizar(new Cliente(salvo.getId(), "Cliente Teste", "Rua Nova", "(21) 99999-0000"));
        conferir("cliente atualizado", clienteService.pesquisar("Cliente Teste").get(0).getEndereco().equals("Rua Nova"));
    }

    private static void testarProduto() throws Exception {
        System.out.println("--- Produto ---");
        ProdutoService produtoService = new ProdutoService();

        try {
            produtoService.cadastrar(new Produto(0, "Produto Teste", -5, 10, 2));
            conferir("preco negativo e recusado", false);
        } catch (RegraNegocioException e) {
            conferir("preco negativo e recusado (" + e.getMessage() + ")", true);
        }

        produtoService.cadastrar(new Produto(0, "Produto Teste", 10.00, 10, 2));
        conferir("produto cadastrado", produtoService.pesquisar("Produto Teste").size() == 1);

        try {
            produtoService.cadastrar(new Produto(0, "Produto Teste", 20.00, 5, 1));
            conferir("nome repetido e recusado", false);
        } catch (RegraNegocioException e) {
            conferir("nome repetido e recusado (" + e.getMessage() + ")", true);
        }
    }

    private static void testarVenda() throws Exception {
        System.out.println("--- Venda ---");
        VendaService vendaService = new VendaService();
        ProdutoService produtoService = new ProdutoService();

        Cliente cliente = new ClienteService().pesquisar("Cliente Teste").get(0);
        Produto produto = produtoService.pesquisar("Produto Teste").get(0);

        Venda venda = new Venda(0, cliente);

        try {
            vendaService.finalizar(venda);
            conferir("venda sem itens e recusada", false);
        } catch (RegraNegocioException e) {
            conferir("venda sem itens e recusada (" + e.getMessage() + ")", true);
        }

        try {
            vendaService.adicionarItem(venda, produto, 50);
            conferir("quantidade maior que o estoque e recusada", false);
        } catch (RegraNegocioException e) {
            conferir("quantidade maior que o estoque e recusada", true);
        }

        vendaService.adicionarItem(venda, produto, 8);
        conferir("total da venda calculado (8 x 10,00 = 80,00)", venda.getTotal() == 80.00);

        vendaService.finalizar(venda);
        produto = produtoService.pesquisar("Produto Teste").get(0);
        conferir("venda baixou o estoque de 10 para 2", produto.getQuantidade() == 2);
        conferir("produto aparece como estoque baixo", produto.isEstoqueBaixo());

        try {
            produtoService.excluir(produto.getId());
            conferir("produto vendido nao pode ser excluido", false);
        } catch (RegraNegocioException e) {
            conferir("produto vendido nao pode ser excluido", true);
        }

        Venda gravada = vendaService.listar("Cliente Teste").get(0);
        conferir("venda gravada com 1 item", vendaService.listarItens(gravada.getId()).size() == 1);

        vendaService.cancelar(gravada.getId());
        produto = produtoService.pesquisar("Produto Teste").get(0);
        conferir("cancelar a venda devolveu o estoque para 10", produto.getQuantidade() == 10);
    }

    private static void testarOrcamento() throws Exception {
        System.out.println("--- Orcamento ---");
        OrcamentoService orcamentoService = new OrcamentoService();
        ProdutoService produtoService = new ProdutoService();

        Cliente cliente = new ClienteService().pesquisar("Cliente Teste").get(0);
        Produto produto = produtoService.pesquisar("Produto Teste").get(0);

        Orcamento orcamento = new Orcamento(0, cliente);
        orcamentoService.adicionarItem(orcamento, produto, 30);
        orcamentoService.salvar(orcamento);

        Orcamento gravado = orcamentoService.listar("Cliente Teste").get(0);
        conferir("orcamento gravado com total 300,00", gravado.getTotal() == 300.00);

        produto = produtoService.pesquisar("Produto Teste").get(0);
        conferir("orcamento nao mexe no estoque", produto.getQuantidade() == 10);

        orcamentoService.excluir(gravado.getId());
        conferir("orcamento excluido", orcamentoService.listar("Cliente Teste").isEmpty());
    }

    private static void limparDadosDeTeste() throws Exception {
        System.out.println("--- Limpeza ---");
        ClienteService clienteService = new ClienteService();
        ProdutoService produtoService = new ProdutoService();

        clienteService.excluir(clienteService.pesquisar("Cliente Teste").get(0).getId());
        produtoService.excluir(produtoService.pesquisar("Produto Teste").get(0).getId());

        conferir("dados de teste apagados",
                clienteService.pesquisar("Cliente Teste").isEmpty()
                && produtoService.pesquisar("Produto Teste").isEmpty());
    }
}
