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
                                    ✅
                                </button>
                            </c:when>
                            <c:otherwise>
                                <button class="action-btn btn-complete" title="Marcar como Concluída"
                                        onclick="window.location.href='${pageContext.request.contextPath}/tarefa?action=complete&id=${tarefa.id}'">
                                    ✓
                                </button>
                            </c:otherwise>
                        </c:choose>
                        
                        <button class="action-btn btn-delete" title="Excluir Tarefa"
                                onclick="if(confirm('Tem certeza que deseja excluir a tarefa: ${tarefa.nome}?')) { 
                                    window.location.href='${pageContext.request.contextPath}/tarefa?action=delete&id=${tarefa.id}' 
                                }">
                            🗑️
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