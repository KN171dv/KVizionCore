package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ProdutoTest {

    @Test
    public void estoqueAcimaDoMinimoNaoEBaixo() {
        Produto produto = new Produto(1, "Cimento", 40.00, 21, 20);

        assertFalse(produto.isEstoqueBaixo());
    }

    @Test
    public void estoqueIgualAoMinimoEBaixo() {
        Produto produto = new Produto(1, "Cimento", 40.00, 20, 20);

        assertTrue(produto.isEstoqueBaixo());
    }

    @Test
    public void estoqueAbaixoDoMinimoEBaixo() {
        Produto produto = new Produto(1, "Cimento", 40.00, 5, 20);

        assertTrue(produto.isEstoqueBaixo());
    }
}
