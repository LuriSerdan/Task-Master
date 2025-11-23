<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">

</head>
<body>

    <div class="container">
        
        <img src="${pageContext.request.contextPath}/assets/img/Logo.png" alt="Logo">
        
        <h2>Login</h2>

        <form action="login" method="post">
            <input type="email" name="email" placeholder="E-mail" required>
            <input type="password" name="senha" placeholder="Senha" required>

            <button type="submit">Entrar</button> 
        </form>

        <a href="${pageContext.request.contextPath}/cadastro">Cadastre-se aqui</a>

        <p class="erro">
            ${erro}
        </p>
    </div>

</body>
</html>
