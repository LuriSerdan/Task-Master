package br.com.taskmaster.controller;

import br.com.taskmaster.dao.UsuarioDAO;
import br.com.taskmaster.model.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// ESTA LINHA ABAIXO É A MÁGICA QUE CONSERTA O SEU ERRO 404
@WebServlet(name = "CadastroController", urlPatterns = {"/cadastro"})
public class CadastroController extends HttpServlet {

    // doGet: Chamado quando você clica no link "Cadastre-se aqui"
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Encaminha para a tela de cadastro (View)
        // Certifique-se que o arquivo cadastro.jsp está dentro de /WEB-INF/views/
        request.getRequestDispatcher("/WEB-INF/views/cadastro.jsp").forward(request, response);
    }

    // doPost: Chamado quando o usuário preenche o formulário e clica em "Salvar"
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        // 1. Pega os dados do formulário
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        
        // 2. Cria o objeto Usuário
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha); // Idealmente criptografar antes, mas para agora ok
        
        // 3. Chama o DAO para salvar (Você precisará criar esse método no UsuarioDAO se não tiver)
        try {
            UsuarioDAO dao = new UsuarioDAO();
            dao.cadastrar(usuario); // <--- Certifique-se que seu DAO tem esse método
            
            // 4. Se deu certo, manda para o Login com mensagem de sucesso
            request.setAttribute("msg", "Cadastro realizado com sucesso! Faça login.");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            
        } catch (Exception e) {
            // Se deu erro, volta pro cadastro e avisa
            request.setAttribute("erro", "Erro ao cadastrar: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/cadastro.jsp").forward(request, response);
        }
    }
}