/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.taskmaster.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession s = request.getSession(false);

        if (s == null || s.getAttribute("usuarioLogado") == null) {
            response.sendRedirect("login");
            return;
        }

        // se estiver logado, mostra a página
        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }
}

