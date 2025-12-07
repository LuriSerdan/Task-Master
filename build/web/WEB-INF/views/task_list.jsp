<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TaskMaster - Tarefas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css">
    
    <style>
        .info-icon {
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

    <header class="header">
        <div class="logo">
            <img src="${pageContext.request.contextPath}/assets/img/Logo.png" alt="Logo">
            TaskMaster
        </div>
        
        <div class="links">
            <nav class="nav-links">
                <a href="${pageContext.request.contextPath}/tarefa?action=new">New Task</a>
                <a href="${pageContext.request.contextPath}/projeto">Projects</a>
            </nav>
            <div class="nav-buttons">
                <button class="log-out-btn" onclick="window.location.href='${pageContext.request.contextPath}/logout'">
                    Log Out
                </button>
            </div>
        </div>
        
    </header>

    <div class="container">
        <div class="main-content">
            <div class="page-header">
                <h1>Tasks</h1>
                <p class="espaco"></p>
                <p>- Visão Geral</p>
            </div>
        
            <div class="actions-bar">
                <select class="dropdown-project" onchange="window.location.href='${pageContext.request.contextPath}/tarefa?action=list&projectId=' + this.value">

                    <option value="0">Todos os Projetos</option>

                    <c:forEach var="p" items="${listaProjetos}">
                        <option value="${p.id}"
                            ${p.id == projetoSelecionado ? "selected" : ""}>
                            ${p.nome}
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>
        

        <div class="task-list">
            <c:forEach var="tarefa" items="${listaTarefas}">
                <div class="task-card">
                    <div class="task-info">
                        <svg class="info-icon" viewBox="0 0 24 24">
                            <circle cx="12" cy="12" r="10"></circle>
                            <line x1="12" y1="16" x2="12" y2="12"></line>
                            <line x1="12" y1="8" x2="12.01" y2="8"></line>
                        </svg>

                        <div class="task-title-area">
                            <h2 class="task-title">${tarefa.nome}</h2>
                            <p class="task-description">${tarefa.descricao}</p>
                            
                            <c:if test="${not empty tarefa.dataEntrega}">
                                <small style="color: #c00;">Entregar até: ${tarefa.dataEntrega}</small>
                            </c:if>
                            
                            <c:if test="${tarefa.statusId == 3}">
                                <small style="color: var(--primary-color); font-weight: bold;"> (Concluída)</small>
                            </c:if>
                        </div>
                    </div>

                    <div class="task-actions">
                        
                        <c:choose>
                            <c:when test="${tarefa.statusId == 3}">
                                <button class="action-btn" style="background-color: #ccc; cursor: default; border-color: #ccc;" title="Já concluída">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="white" viewBox="0 0 16 16">
                                      <path d="M12.736 3.97a.733.733 0 0 1 1.047 0c.286.289.29.756.01 1.05L7.88 12.01a.733.733 0 0 1-1.04-.02L2.003 7.825a.733.733 0 0 1 1.04-.022L7.003 10.5V4.74z"/>
                                    </svg>
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button class="action-btn btn-complete" title="Marcar como Concluída"
                                        onclick="window.location.href='${pageContext.request.contextPath}/tarefa?action=complete&id=${tarefa.id}'">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                                      <path d="M12.736 3.97a.733.733 0 0 1 1.047 0c.286.289.29.756.01 1.05L7.88 12.01a.733.733 0 0 1-1.04-.02L2.003 7.825a.733.733 0 0 1 1.04-.022L7.003 10.5V4.74z"/>
                                    </svg>
                                </button>
                            </c:otherwise>
                        </c:choose>
                        
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
            
            <c:if test="${empty listaTarefas}">
                <p style="text-align: center; margin-top: 50px; color: #777;">
                    Nenhuma tarefa encontrada. Clique em "New Task" para começar!
                </p>
            </c:if>
        </div>
    </div>

</body>
</html>