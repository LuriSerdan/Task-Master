<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TaskMaster - Tarefas</title>
    <!-- Incluindo o CSS que criamos na estrutura de assets -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    
    <!-- Ícone de Informação (Simulado com SVG para o design) -->
    <style>
        .info-icon {
            /* Usando um SVG inline simples para o ícone de informação do mockup */
            display: inline-block;
            width: 24px;
            height: 24px;
            fill: none;
            stroke: currentColor;
            stroke-width: 2;
            stroke-linecap: round;
            stroke-linejoin: round;
        }
    </style>
</head>
<body>

    <!-- Header (Navegação) -->
    <header class="header">
        <div class="logo">
            <div class="logo-icon"></div>
            TaskMaster
        </div>
        <nav class="nav-links">
            <a href="${pageContext.request.contextPath}/tarefa?action=new">New Task</a>
            <a href="#">Projects</a>
        </nav>
        <div class="nav-buttons">
            <button class="log-out-btn">Log Out</button>
        </div>
    </header>

    <div class="container">
        <!-- Título e Contexto -->
        <div class="page-header">
            <h1>Tasks</h1>
            <!-- Usando a EL para exibir o nome do projeto atual (simulado) -->
            <p>Project: Minha Casa Própria</p>
        </div>
        
        <!-- Barra de Ações (Filtro de Projetos) -->
        <div class="actions-bar">
            <select class="dropdown-project" onchange="window.location.href='${pageContext.request.contextPath}/tarefa?action=list&projectId=' + this.value">
                <option value="0">Todos os Projetos</option> <option value="1" ${selectedProjectId == '1' ? 'selected' : ''}>Projeto 1</option>
                <option value="2" ${selectedProjectId == '2' ? 'selected' : ''}>Minha Casa Própria</option>
                <option value="3" ${selectedProjectId == '3' ? 'selected' : ''}>Outro Projeto</option>
            </select>
        </div>

        <!-- LISTA DE TAREFAS (JSTL Loop) -->
        <div class="task-list">
            <c:forEach var="tarefa" items="${listaTarefas}">
                <div class="task-card">
                    <div class="task-info">
                        <!-- Ícone de Informação/Detalhes -->
                        <svg class="info-icon" viewBox="0 0 24 24">
                            <circle cx="12" cy="12" r="10"></circle>
                            <line x1="12" y1="16" x2="12" y2="12"></line>
                            <line x1="12" y1="8" x2="12.01" y2="8"></line>
                        </svg>

                        <div class="task-title-area">
                            <!-- Usando EL para exibir Nome e Descrição da Tarefa -->
                            <h2 class="task-title">${tarefa.nome}</h2>
                            <p class="task-description">${tarefa.descricao}</p>
                            
                            <!-- Exibição Condicional de Data de Entrega/Status (Exemplo de Validação EL) -->
                            <c:if test="${not empty tarefa.dataEntrega}">
                                <small style="color: #c00;">Entregar até: ${tarefa.dataEntrega}</small>
                            </c:if>
                            
                            <!-- Exemplo de como você pode usar o statusId para estilizar -->
                            <c:if test="${tarefa.statusId == 3}">
                                <small style="color: var(--primary-color); font-weight: bold;"> (Concluída)</small>
                            </c:if>
                        </div>
                    </div>

                    <div class="task-actions">
                        <!-- Botão de Concluir (Simulado com V de check) -->
                        <button class="action-btn btn-complete" title="Marcar como Concluída">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                              <path d="M12.736 3.97a.733.733 0 0 1 1.047 0c.286.289.29.756.01 1.05L7.88 12.01a.733.733 0 0 1-1.04-.02L2.003 7.825a.733.733 0 0 1 1.04-.022L7.003 10.5V4.74z"/>
                            </svg>
                        </button>
                        
                        <!-- Botão de Deletar (Simulado com Lixeira) -->
                        <!-- A URL aqui apontaria para o Controller, ex: /tarefa?action=delete&id=${tarefa.id} -->
                        <button class="action-btn btn-delete" title="Excluir Tarefa"
                                onclick="if(confirm('Tem certeza que deseja excluir a tarefa: ${tarefa.nome}?')) { 
                                    window.location.href='${pageContext.request.contextPath}/tarefa?action=delete&id=${tarefa.id}' 
                                }">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                                <path d="M6.5 1h3a.5.5 0 0 1 .5.5v1H6v-1a.5.5 0 0 1 .5-.5ZM11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3A1.5 1.5 0 0 0 5 1.5v1H2.5a.5.5 0 0 0 0 1h.538l.853 10.66A2 2 0 0 0 4.885 16h6.23a2 2 0 0 0 1.994-1.84l.853-10.66h.538a.5.5 0 0 0 0-1H11Zm1.715 6.096-.853 10.66a1 1 0 0 1-.994.94H4.885a1 1 0 0 1-.994-.94l-.853-10.66h9.713ZM9 5.5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0v-6Zm2 0a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0v-6Z"/>
                            </svg>
                        </button>
                    </div>
                </div>
            </c:forEach>
            
            <!-- Exemplo de JSTL/EL: Se a lista estiver vazia -->
            <c:if test="${empty listaTarefas}">
                <p style="text-align: center; margin-top: 50px; color: #777;">
                    Nenhuma tarefa encontrada. Clique em "New Task" para começar!
                </p>
            </c:if>
        </div>
    </div>

</body>
</html>