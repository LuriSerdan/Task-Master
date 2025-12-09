package br.com.taskmaster.controller;

import br.com.taskmaster.dao.UsuarioDAO;
import br.com.taskmaster.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet; // <--- Faltava esse import
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// AQUI ESTAVA O PROBLEMA: Faltava a anotação de mapeamento
@WebServlet(name = "RegistrarUsuarioController", urlPatterns = {"/registrarUsuario"})
public class RegistrarUsuarioController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Importante para não bugar acentos no nome
        request.setCharacterEncoding("UTF-8");

        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String funcao = request.getParameter("funcao");

        Usuario novo = new Usuario();
        novo.setNome(nome);
        novo.setEmail(email);
        novo.setSenha(senha);
        novo.setFuncao(funcao);

        UsuarioDAO dao = new UsuarioDAO();

        try {
            // O método cadastrar é void. Se der erro, ele vai pro catch.
            // Se passar direto, é porque deu sucesso.
            dao.cadastrar(novo); 
            
            // Sucesso: Manda pro login
            response.sendRedirect("login"); 

        } catch (Exception e) {
            // Erro: Volta pro formulário e mostra a mensagem
            e.printStackTrace(); // Ajuda a ver o erro no console do NetBeans
            request.setAttribute("erro", "Erro ao cadastrar: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/cadastro.jsp").forward(request, response);
        }
    }
}