package br.com.taskmaster.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LogoutController", urlPatterns = {"/logout"})
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Pega a sessão atual, se existir
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            session.invalidate(); // Destroi os dados do usuário logado
        }
        
        // Redireciona para a tela de login
        response.sendRedirect("login");
    }
}