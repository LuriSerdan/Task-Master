<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>TaskMaster - Projects</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css">
    
    <style>
        .btn-new-project {
            background-color: #28a745;
            color: white;
            padding: 8px 15px;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
            font-size: 14px;
        }
        .btn-new-project:hover {
            background-color: #218838;
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
            <a href="${pageContext.request.contextPath}/tarefa">Tasks</a>
            <a href="${pageContext.request.contextPath}/projeto" style="font-weight: bold;">Projects</a>
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
            <h1>Projects</h1>
            <p class="espaco"></p>
            <p>- Gerenciamento</p>
        </div>
        
        <div class="actions-bar">
             <a href="${pageContext.request.contextPath}/projeto?action=new" class="btn-new-project">
                + New Project
             </a>
        </div>
    </div>

    <div class="task-list">

        <c:forEach var="p" items="${listaProjetos}">
            <div class="task-card">
                
                <div class="task-info">

                    <div class="task-title-area">
                        <h3 class="task-title">${p.nome}</h3>

                        <div class="task-description">
                            Start: <b>${p.dataInicio}</b> 
                            &nbsp; • &nbsp; 
                            End: <b>${empty p.dataFim ? "Undefined" : p.dataFim}</b>
                        </div>
                    </div>
                </div>

                <div class="task-actions">
                    <button class="action-btn btn-edit" title="Editar"
                        onclick="window.location.href='${pageContext.request.contextPath}/projeto?action=edit&id=${p.id}'">
                        Edit
                    </button>
                    
                    <button class="action-btn btn-delete" title="Excluir Projeto"
                            onclick="if(confirm('Tem certeza que deseja excluir o projeto: ${p.nome}?')) { 
                                window.location.href='${pageContext.request.contextPath}/projeto?action=delete&id=${p.id}' 
                            }">
                        Delete
                    </button>
                </div>

            </div>
        </c:forEach>

        <c:if test="${empty listaProjetos}">
            <p style="text-align: center; margin-top: 50px; color: #777;">
                Nenhum projeto cadastrado. Clique em "+ New Project" para começar!
            </p>
        </c:if>

    </div>
</div>

</body>
</html>