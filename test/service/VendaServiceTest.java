package service;

import static org.junit.Assert.assertEquals;
import model.Cliente;
import model.Produto;
import model.Venda;
import org.junit.Before;
import org.junit.Test;

// Testa a regra de colocar itens na venda, que nao precisa do banco de dados
public class VendaServiceTest {

    private VendaService vendaService = new VendaService();
    private Venda venda;
    private Produto tijolo;

    @Before
    public void preparar() {
        Cliente cliente = new Cliente(1, "Carlos Silva", "Rua A", "99999-1111");
        venda = new Venda(0, cliente);
        tijolo = new Produto(3, "Tijolo", 1.50, 10, 2); // 10 em estoque
    }

    @Test
    public void adicionaItemQuandoTemEstoque() throws RegraNegocioException {
        vendaService.adicionarItem(venda, tijolo, 4);

        assertEquals(1, venda.getItens().size());
        assertEquals(6.00, venda.getTotal(), 0.001);
    }

    @Test(expected = RegraNegocioException.class)
    public void quantidadeZeroERecusada() throws RegraNegocioException {
        vendaService.adicionarItem(venda, tijolo, 0);
    }

    @Test(expected = RegraNegocioException.class)
    public void quantidadeMaiorQueOEstoqueERecusada() throws RegraNegocioException {
        vendaService.adicionarItem(venda, tijolo, 11);
    }

    @Test
    public void naoDeixaPassarDoEstoqueSomandoItensRepetidos() throws RegraNegocioException {
        vendaService.adicionarItem(venda, tijolo, 6);

        try {
            vendaService.adicionarItem(venda, tijolo, 5); // 6 + 5 = 11, so tem 10
        } catch (RegraNegocioException e) {
            // esperado: a venda continua so com o primeiro item
        }

        assertEquals(1, venda.getItens().size());
        assertEquals(6, venda.getQuantidadeDoProduto(tijolo.getId()));
    }

    @Test
    public void aceitaVenderExatamenteOEstoqueTodo() throws RegraNegocioException {
        vendaService.adicionarItem(venda, tijolo, 10);

        assertEquals(15.00, venda.getTotal(), 0.001);
    }
}
