package br.com.taskmaster.controller;

import br.com.taskmaster.dao.ProjetoDAO;
import br.com.taskmaster.dao.UsuarioDAO;
import br.com.taskmaster.model.Projeto;
import br.com.taskmaster.model.Usuario;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "ProjectController", urlPatterns = {"/projeto"})
public class ProjectController extends HttpServlet {

    private ProjetoDAO projetoDAO;
    private UsuarioDAO usuarioDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        projetoDAO = new ProjetoDAO();
        usuarioDAO = new UsuarioDAO();
    }

    // --- MÉTODO DE SEGURANÇA (igual ao da tarefa) ---
    private Usuario verificarUsuarioLogado(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            response.sendRedirect("login");
            return null;
        }
        return usuario;
    }

    // ===================================================================
    // GET
    // ===================================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (verificarUsuarioLogado(request, response) == null) return;

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                mostrarFormularioNovoProjeto(request, response);
                break;
            
            // --- NOVO CASE PARA EDIÇÃO ---
            case "edit":
                mostrarFormularioEdicao(request, response);
                break;

            case "delete":
                removerProjeto(request, response);
                break;

            case "list":
            default:
                listarProjetos(request, response);
                break;
        }
    }

    // ===================================================================
    // POST
    // ===================================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuarioLogado = verificarUsuarioLogado(request, response);
        if (usuarioLogado == null) return;

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        // Agora aceitamos "save" (que serve para insert e update) ou mantemos compatibilidade se vier insert
        if ("save".equals(action) || "insert".equals(action)) {
            salvarProjeto(request, response, usuarioLogado);
        } else {
            listarProjetos(request, response);
        }
    }

    // ===================================================================
    // MÉTODOS AUXILIARES (IGUAL AO PADRÃO DO TAREFACONTROLLER)
    // ===================================================================

    private void listarProjetos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuarioLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");
        
        List<Projeto> listaProjetos = projetoDAO.listarPorUsuario(
            usuarioLogado.getId(), 
            usuarioLogado.getFuncao()
        );
        
        request.setAttribute("listaProjetos", listaProjetos);

        request.getRequestDispatcher("/WEB-INF/views/project_list.jsp").forward(request, response);
    }

    private void mostrarFormularioNovoProjeto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Usuario> listaUsuarios = usuarioDAO.listar();
        
        request.setAttribute("usuarios", listaUsuarios);
        request.getRequestDispatcher("/WEB-INF/views/project_form.jsp").forward(request, response);
    }
    
    private void salvarProjeto(HttpServletRequest request, HttpServletResponse response, Usuario usuarioLogado)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        
        String nome = request.getParameter("nome");
        String dataInicioStr = request.getParameter("data_inicio");
        String dataFimStr = request.getParameter("data_fim");
        String liderIdStr = request.getParameter("lider_id");

        Projeto p = new Projeto();
        if (idStr != null && !idStr.isEmpty()) {
            p.setId(Integer.parseInt(idStr));
        }
        
        p.setNome(nome);

        try {
            if (dataInicioStr != null && !dataInicioStr.isEmpty()) {
                p.setDataInicio(java.time.LocalDate.parse(dataInicioStr)); 
            }
            if (dataFimStr != null && !dataFimStr.isEmpty()) {
                p.setDataFim(java.time.LocalDate.parse(dataFimStr));
            }
        } catch (Exception e) {
            System.out.println("Erro data: " + e.getMessage());
        }

        if (liderIdStr != null && !liderIdStr.isEmpty())
            p.setLiderId(Integer.parseInt(liderIdStr));

        String[] membrosSelecionados = request.getParameterValues("membros");
        
        if (membrosSelecionados != null) {
            for (String idMembro : membrosSelecionados) {
                p.adicionarMembro(Integer.parseInt(idMembro));
            }
        }

        // DECISÃO: INSERT OU UPDATE?
        if (p.getId() > 0) {
            projetoDAO.atualizar(p);
        } else {
            projetoDAO.inserir(p);
        }

        response.sendRedirect("projeto?action=list");
    }

    private void removerProjeto(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String idStr = request.getParameter("id");

        if (idStr != null && !idStr.isEmpty()) {
            int id = Integer.parseInt(idStr);
            projetoDAO.deletar(id);
        }

        response.sendRedirect("projeto?action=list");
    }
    
    private void mostrarFormularioEdicao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        Projeto projeto = projetoDAO.buscarPorId(id);
        request.setAttribute("projeto", projeto);

        List<Usuario> listaUsuarios = usuarioDAO.listar();
        request.setAttribute("usuarios", listaUsuarios);
        
        request.getRequestDispatcher("/WEB-INF/views/project_form.jsp").forward(request, response);
    }
}
