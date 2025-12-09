<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>TaskMaster - Nova Tarefa</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css">
</head>
<body>

    <header class="header">
        <div class="logo">TaskMaster</div>
        <nav class="nav-links">
            <a href="${pageContext.request.contextPath}/tarefa">Voltar para Lista</a>
        </nav>
    </header>

    <div class="container">
        <div class="page-header">
            <h1>Nova Tarefa</h1>
        </div>

        <div class="form-container">
            <form action="${pageContext.request.contextPath}/tarefa" method="post">
                <input type="hidden" name="action" value="insert">
                
                <div class="form-group">
                    <label for="nome">Título da Tarefa:</label>
                    <input type="text" id="nome" name="nome" class="form-input" required>
                </div>

                <div class="form-group">
                    <label for="descricao">Descrição:</label>
                    <textarea id="descricao" name="descricao" class="form-textarea" rows="4"></textarea>
                </div>

                <div class="form-group">
                    <label for="dataEntrega">Data de Entrega:</label>
                    <input type="date" id="dataEntrega" name="dataEntrega" class="form-input">
                </div>
                
                <div class="form-group">
                    <label for="projetoId">Projeto:</label>
                    <select id="projetoId" name="projetoId" class="form-select" required>
                        <option value="">Selecione um projeto</option>
                        <c:forEach var="p" items="${projetos}">
                            <option value="${p.id}">${p.nome}</option>
                        </c:forEach>
                    </select>
                </div>

                <button type="submit" class="form-submit-btn">
                    Salvar Tarefa
                </button>
            </form>
        </div>
    </div>

</body>
</html>