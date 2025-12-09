<%-- 
    Document   : cadastro
    Created on : 22 de nov. de 2025, 20:39:52
    Author     : damns
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cadastro de Usuário</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cadastro.css">
</head>
<body>
    
<div class="container">
    <img src="${pageContext.request.contextPath}/assets/img/Logo.png" alt="Logo">
    <h2>Cadastro de Usuário</h2>

    <form action="registrarUsuario" method="post">

        <label>Nome:</label>
        <input type="text" name="nome" required><br>

        <label>E-mail:</label>
        <input type="email" name="email" required><br>

        <label>Senha:</label>
        <input type="password" name="senha" required><br>

        <label>Função:</label>
        <select name="funcao" required>
            <option value="">Selecione uma função</option>
            <option value="Desenvolvedor">Desenvolvedor</option>
            <option value="Analista">Analista</option>
            <option value="Gerente de Projeto">Gerente de Projeto</option>
            <option value="Gerente">Gerente</option>
            <option value="Designer">Designer</option>
            <option value="Tester">Tester</option>
        </select><br><br>

        <button type="submit">Cadastrar</button>
    </form>
    <a href="${pageContext.request.contextPath}/login">Cancelar</a>

</div>

</body>
</html>

