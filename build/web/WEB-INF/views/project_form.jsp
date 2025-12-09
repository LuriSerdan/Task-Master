<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>TaskMaster - ${not empty projeto.id ? 'Editar Projeto' : 'Novo Projeto'}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/home.css">
</head>
<body>

<header class="header">
    <div class="logo">TaskMaster</div>
    <nav class="nav-links">
        <a href="${pageContext.request.contextPath}/projeto">Voltar para Lista</a>
    </nav>
</header>

<div class="container">
    <div class="page-header">
        <h1>${not empty projeto.id ? 'Editar Projeto' : 'Novo Projeto'}</h1>
    </div>

    <div class="form-container">
        <form action="${pageContext.request.contextPath}/projeto" method="post">
            
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="id" value="${projeto.id}">
            
            <div class="form-group">
                <label for="nome">Nome do Projeto:</label>
                <input type="text" id="nome" name="nome" value="${projeto.nome}" class="form-input" required>
            </div>

            <div class="form-group">
                <label for="data_inicio">Data de Início:</label>
                <input type="date" id="data_inicio" name="data_inicio" value="${projeto.dataInicio}" class="form-input" required>
            </div>

            <div class="form-group">
                <label for="data_fim">Data de Fim:</label>
                <input type="date" id="data_fim" name="data_fim" value="${projeto.dataFim}" class="form-input">
            </div>

            <div class="form-group">
                <label for="lider_id">Líder do Projeto:</label>
                <select id="lider_id" name="lider_id" class="form-select" required>
                    <option value="">Selecione um usuário</option>
                    <c:forEach var="u" items="${usuarios}">
                        <option value="${u.id}" ${u.id == projeto.liderId ? 'selected' : ''}>
                            ${u.nome}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label>Membros da Equipe:</label>
                <div class="checkbox-group">
                    <c:forEach var="u" items="${usuarios}">
                        <div class="checkbox-item">
                            <input type="checkbox" 
                                   name="membros" 
                                   value="${u.id}" 
                                   id="user_${u.id}"
                                   <c:forEach var="membroId" items="${projeto.membrosIds}">
                                       <c:if test="${membroId == u.id}">checked</c:if>
                                   </c:forEach>
                            >
                            <label for="user_${u.id}">${u.nome}</label>
                        </div>
                    </c:forEach>

                    <c:if test="${empty usuarios}">
                        <div class="empty-state">Nenhum usuário disponível para adicionar.</div>
                    </c:if>
                </div>
                <div class="form-helper-text">Marque quem participará deste projeto.</div>
            </div>

            <button type="submit" class="form-submit-btn">
                Salvar Projeto
            </button>
        </form>
    </div>
</div>

</body>
</html>