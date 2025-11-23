package br.com.taskmaster.controller;

import br.com.taskmaster.dao.TarefaDAO;
import br.com.taskmaster.model.Tarefa;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TarefaController", urlPatterns = {"/tarefa"})
public class TarefaController extends HttpServlet {

    private TarefaDAO tarefaDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        tarefaDAO = new TarefaDAO();
    }

    // GET: Para navegação (Listar ou Abrir Formulário)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                mostrarFormularioNovaTarefa(request, response);
                break;
            case "delete": // <--- ADICIONE ESTE CASO
                removerTarefa(request, response);
                break;
            case "list":
            default:
                listarTarefas(request, response);
                break;
        }
    }

    // POST: Para receber dados de formulários (Insert, Update, Delete)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Importante para aceitar acentos vindo do formulário
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        
        if ("insert".equals(action)) {
            inserirTarefa(request, response);
        } else {
            listarTarefas(request, response);
        }
    }
    
    // --- MÉTODOS AUXILIARES ---

    private void listarTarefas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Tarefa> listaTarefas;
        
        // 1. Verifica se veio o parametro "projectId" do filtro (lá do JSP)
        String projetoIdStr = request.getParameter("projectId");
        
        // 2. Decide qual método do DAO chamar
        if (projetoIdStr != null && !projetoIdStr.isEmpty() && !projetoIdStr.equals("0")) {
            // Se tem ID, chama o método com filtro (WHERE)
            int projetoId = Integer.parseInt(projetoIdStr);
            listaTarefas = tarefaDAO.listarPorProjeto(projetoId);
            
            // Devolve o ID para o JSP saber qual option deixar marcada (selected)
            request.setAttribute("selectedProjectId", projetoIdStr);
        } else {
            // Se não tem ID (ou é "0" - Todos), traz tudo
            listaTarefas = tarefaDAO.listar();
        }

        // 3. Manda pra View
        request.setAttribute("listaTarefas", listaTarefas);
        request.getRequestDispatcher("/WEB-INF/views/task_list.jsp").forward(request, response);
    }

    private void inserirTarefa(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Pegar parâmetros do form
        String nome = request.getParameter("nome");
        String descricao = request.getParameter("descricao");
        String dataEntregaStr = request.getParameter("dataEntrega");
        String projetoIdStr = request.getParameter("projetoId");

        // 2. Preencher objeto Tarefa
        Tarefa t = new Tarefa();
        t.setNome(nome);
        t.setDescricao(descricao);
        t.setDataCriacao(new Date()); // Data de hoje
        t.setStatusId(1); // 1 = Pendente

        // Tratamento da Data de Entrega (vem como String yyyy-MM-dd do HTML5)
        try {
            if (dataEntregaStr != null && !dataEntregaStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                t.setDataEntrega(sdf.parse(dataEntregaStr));
            }
        } catch (Exception e) {
            System.out.println("Erro ao converter data: " + e.getMessage());
        }

        // Tratamento do ID do Projeto
        if (projetoIdStr != null && !projetoIdStr.isEmpty()) {
            t.setProjetoId(Integer.parseInt(projetoIdStr));
        }

        // 3. Chamar DAO
        tarefaDAO.inserir(t);

        // 4. Redirecionar para a lista (Evita reenvio de form ao dar F5)
        response.sendRedirect("tarefa?action=list");
    }
    
    // Faltou esse método aqui para abrir o JSP de cadastro
    private void mostrarFormularioNovaTarefa(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Apenas encaminha para a página do formulário
        request.getRequestDispatcher("/WEB-INF/views/task_form.jsp").forward(request, response);
    }
    
    private void removerTarefa(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Pega o ID da URL (?action=delete&id=10)
        String idStr = request.getParameter("id");
        
        if (idStr != null && !idStr.isEmpty()) {
            int id = Integer.parseInt(idStr);
            
            // 2. Chama o DAO para deletar
            tarefaDAO.deletar(id);
        }

        // 3. Redireciona de volta para a lista
        response.sendRedirect("tarefa?action=list");
    }
    
}