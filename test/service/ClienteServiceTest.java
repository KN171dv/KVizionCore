package service;

import model.Cliente;
import org.junit.Test;

// Testa so a validacao, que nao precisa do banco de dados
public class ClienteServiceTest {

    private ClienteService clienteService = new ClienteService();

    @Test
    public void clienteValidoPassaNaValidacao() throws RegraNegocioException {
        Cliente cliente = new Cliente(0, "Maria Souza", "Rua B, 20", "(21) 99999-2222");

        clienteService.validar(cliente); // nao pode lancar excecao
    }

    @Test(expected = RegraNegocioException.class)
    public void nomeVazioERecusado() throws RegraNegocioException {
        clienteService.validar(new Cliente(0, "   ", "Rua B", "99999-2222"));
    }

    @Test(expected = RegraNegocioException.class)
    public void nomeComMaisDe100CaracteresERecusado() throws RegraNegocioException {
        String nomeGrande = "a".repeat(101);

        clienteService.validar(new Cliente(0, nomeGrande, "Rua B", "99999-2222"));
    }

    @Test(expected = RegraNegocioException.class)
    public void telefoneVazioERecusado() throws RegraNegocioException {
        clienteService.validar(new Cliente(0, "Maria Souza", "Rua B", ""));
    }

    @Test(expected = RegraNegocioException.class)
    public void telefoneComLetrasERecusado() throws RegraNegocioException {
        clienteService.validar(new Cliente(0, "Maria Souza", "Rua B", "telefone"));
    }
}
