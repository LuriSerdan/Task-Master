package br.com.taskmaster.controller;

import br.com.taskmaster.dao.TarefaDAO;
import br.com.taskmaster.model.Tarefa;
import br.com.taskmaster.model.Usuario;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "TarefaController", urlPatterns = {"/tarefa"})
public class TarefaController extends HttpServlet {

    private TarefaDAO tarefaDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        tarefaDAO = new TarefaDAO();
    }

    // --- Método auxiliar de segurança ---
    private Usuario verificarUsuarioLogado(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        
        if (usuario == null) {
            // Se não estiver logado, manda para a tela de login
            response.sendRedirect("login"); // Certifique-se que o LoginController atende em /login
            return null;
        }
        return usuario;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Verifica segurança antes de qualquer coisa
        if (verificarUsuarioLogado(request, response) == null) return;

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                mostrarFormularioNovaTarefa(request, response);
                break;
            case "delete":
                removerTarefa(request, response);
                break;
            case "complete": // <--- ADICIONE AQUI
                concluirTarefa(request, response);
                break;
            case "list":
            default:
                listarTarefas(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Verifica segurança e captura o usuário para usar no insert
        Usuario usuarioLogado = verificarUsuarioLogado(request, response);
        if (usuarioLogado == null) return;

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        
        if ("insert".equals(action)) {
            inserirTarefa(request, response, usuarioLogado);
        } else {
            listarTarefas(request, response);
        }
    }
    
    // --- MÉTODOS AUXILIARES ---

    private void listarTarefas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Pega o usuário da sessão (já validamos no doGet que ele existe)
        HttpSession session = request.getSession();
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        
        List<Tarefa> listaTarefas;
        String projetoIdStr = request.getParameter("projectId");
        
        if (projetoIdStr != null && !projetoIdStr.isEmpty() && !projetoIdStr.equals("0")) {
            int projetoId = Integer.parseInt(projetoIdStr);
            // Agora passamos o ID do projeto E o ID do usuário
            listaTarefas = tarefaDAO.listarPorProjeto(projetoId, usuarioLogado.getId());
            request.setAttribute("selectedProjectId", projetoIdStr);
        } else {
            // Chama o método novo que filtra por usuário
            listaTarefas = tarefaDAO.listarPorUsuario(usuarioLogado.getId());
        }

        request.setAttribute("listaTarefas", listaTarefas);
        request.getRequestDispatcher("/WEB-INF/views/task_list.jsp").forward(request, response);
    }

    private void mostrarFormularioNovaTarefa(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/task_form.jsp").forward(request, response);
    }

    private void inserirTarefa(HttpServletRequest request, HttpServletResponse response, Usuario usuarioLogado)
            throws ServletException, IOException {
        
        String nome = request.getParameter("nome");
        String descricao = request.getParameter("descricao");
        String dataEntregaStr = request.getParameter("dataEntrega");
        String projetoIdStr = request.getParameter("projetoId");

        Tarefa t = new Tarefa();
        t.setNome(nome);
        t.setDescricao(descricao);
        t.setDataCriacao(new Date());
        t.setStatusId(1); // Pendente
        
        // Define o usuário logado como responsável automaticamente
        t.setResponsavelId(usuarioLogado.getId());

        try {
            if (dataEntregaStr != null && !dataEntregaStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                t.setDataEntrega(sdf.parse(dataEntregaStr));
            }
        } catch (Exception e) {
            System.out.println("Erro ao converter data: " + e.getMessage());
        }

        if (projetoIdStr != null && !projetoIdStr.isEmpty()) {
            t.setProjetoId(Integer.parseInt(projetoIdStr));
        }

        tarefaDAO.inserir(t);
        response.sendRedirect("tarefa?action=list");
    }

    private void removerTarefa(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            int id = Integer.parseInt(idStr);
            tarefaDAO.deletar(id);
        }
        response.sendRedirect("tarefa?action=list");
    }
    
    private void concluirTarefa(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr != null && !idStr.isEmpty()) {
            int id = Integer.parseInt(idStr);
            tarefaDAO.concluir(id);
        }
        
        // Redireciona para a lista para ver a tarefa atualizada
        response.sendRedirect("tarefa?action=list");
    }
    
}