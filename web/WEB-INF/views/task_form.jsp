<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>TaskMaster - Nova Tarefa</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
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

        <div class="task-card" style="padding: 20px;">
            <form action="${pageContext.request.contextPath}/tarefa" method="post">
                <input type="hidden" name="action" value="insert">

                <div style="margin-bottom: 15px;">
                    <label>Título da Tarefa:</label><br>
                    <input type="text" name="nome" required style="width: 100%; padding: 8px;">
                </div>

                <div style="margin-bottom: 15px;">
                    <label>Descrição:</label><br>
                    <textarea name="descricao" rows="4" style="width: 100%; padding: 8px;"></textarea>
                </div>

                <div style="margin-bottom: 15px;">
                    <label>Data de Entrega:</label><br>
                    <input type="date" name="dataEntrega" style="padding: 8px;">
                </div>

                <div style="margin-bottom: 15px;">
                    <label>Projeto:</label><br>
                    <select name="projetoId" style="padding: 8px;">
                        <option value="1">Projeto 1</option>
                        <option value="2">Minha Casa Própria</option>
                    </select>
                </div>
                
                <button type="submit" class="action-btn" style="background-color: #28a745; color: white; width: auto; padding: 10px 20px;">
                    Salvar Tarefa
                </button>
            </form>
        </div>
    </div>

</body>
</html>