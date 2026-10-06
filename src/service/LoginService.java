package service;

import dao.UsuarioDAO;
import java.sql.SQLException;
import model.Usuario;

public class LoginService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario autenticar(String login, String senha) throws RegraNegocioException, SQLException {
        if (login == null || login.trim().isEmpty() || senha == null || senha.isEmpty()) {
            throw new RegraNegocioException("Informe o login e a senha.");
        }

        Usuario usuario = usuarioDAO.autenticar(login.trim(), senha);
        if (usuario == null) {
            throw new RegraNegocioException("Login ou senha inválidos.");
        }
        return usuario;
    }
}
