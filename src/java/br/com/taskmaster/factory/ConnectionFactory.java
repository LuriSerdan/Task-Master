/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.taskmaster.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL =
        "jdbc:mysql://localhost:3306/taskmaster?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root"; 
    private static final String PASSWORD = ""; // coloque sua senha se tiver

    private ConnectionFactory() {}

    public static Connection getConnection() {

        try {
            // FORÇA O CARREGAMENTO DO DRIVER
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver JDBC do MySQL NÃO encontrado! -> " + e.getMessage());

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco -> " + e.getMessage(), e);
        }
    }
}