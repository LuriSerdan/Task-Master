<%-- 
    Document   : home
    Created on : 22 de nov. de 2025, 20:34:51
    Author     : damns
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home</title>
</head>
<body>

    <h1>Bem-vindo(a), <%= session.getAttribute("usuarioLogado") != null 
            ? ((br.com.taskmaster.model.Usuario) session.getAttribute("usuarioLogado")).getNome()
            : "usuário" %>!</h1>

    <p>Login realizado com sucesso!</p>


</body>
</html>

