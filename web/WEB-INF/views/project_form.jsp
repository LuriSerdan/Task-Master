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

    <div class="task-card" style="padding: 20px;">
        <form action="${pageContext.request.contextPath}/projeto" method="post">
            
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="id" value="${projeto.id}">
            
            <div style="margin-bottom: 15px;">
                <label>Nome do Projeto:</label><br>
                <input type="text" name="nome" value="${projeto.nome}" required style="width: 100%; padding: 8px;">
            </div>

            <div style="margin-bottom: 15px;">
                <label>Data de Início:</label><br>
                <input type="date" name="data_inicio" value="${projeto.dataInicio}" required style="padding: 8px;">
            </div>

            <div style="margin-bottom: 15px;">
                <label>Data de Fim:</label><br>
                <input type="date" name="data_fim" value="${projeto.dataFim}" style="padding: 8px;">
            </div>

            <div style="margin-bottom: 15px;">
                <label>Líder do Projeto:</label><br>
                <select name="lider_id" required style="width: 100%; padding: 8px;">
                    <option value="">Selecione um usuário</option>

                    <c:forEach var="u" items="${usuarios}">
                        <option value="${u.id}" ${u.id == projeto.liderId ? 'selected' : ''}>
                            ${u.nome}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div style="margin-bottom: 20px;">
                <label>Membros da Equipe:</label><br>
                
                <div style="border: 1px solid #ccc; padding: 10px; max-height: 150px; overflow-y: auto; background: #fff; border-radius: 4px; margin-top: 5px;">

                    <c:forEach var="u" items="${usuarios}">
                        <div style="margin-bottom: 5px; display: flex; align-items: center;">
                            
                            <input type="checkbox" 
                                   name="membros" 
                                   value="${u.id}" 
                                   id="user_${u.id}"
                                   style="margin-right: 8px;"

                                   /* Lógica: Verifica se o ID desse usuário está na lista do projeto */
                                   <c:forEach var="membroId" items="${projeto.membrosIds}">
                                       <c:if test="${membroId == u.id}">checked</c:if>
                                   </c:forEach>
                            >
                            
                            <label for="user_${u.id}" style="font-weight: normal; cursor: pointer; margin: 0;">
                                ${u.nome}
                            </label>
                        </div>
                    </c:forEach>

                    <c:if test="${empty usuarios}">
                        <span style="color: #777; font-size: 0.9em;">Nenhum usuário disponível para adicionar.</span>
                    </c:if>
                </div>
                <small style="color: #666; font-size: 0.85rem;">Marque quem participará deste projeto.</small>
            </div>
            <button type="submit" class="action-btn"
                    style="background-color: #28a745; color: white; width: auto; padding: 10px 20px;">
                Salvar Projeto
            </button>
        </form>
    </div>
</div>

</body>
</html>