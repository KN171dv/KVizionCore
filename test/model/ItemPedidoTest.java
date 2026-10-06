package model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ItemPedidoTest {

    @Test
    public void subtotalDeveSerPrecoVezesQuantidade() {
        Produto cimento = new Produto(1, "Cimento", 40.00, 100, 20);

        ItemPedido item = new ItemPedido(cimento, 3);

        assertEquals(120.00, item.getSubtotal(), 0.001);
    }

    @Test
    public void itemVindoDoBancoMantemSubtotalGravado() {
        // o preco do produto mudou depois da venda, mas o subtotal gravado continua o mesmo
        Produto cimento = new Produto(1, "Cimento", 45.00, 100, 20);

        ItemPedido item = new ItemPedido(cimento, 3, 120.00);

        assertEquals(120.00, item.getSubtotal(), 0.001);
    }
}
