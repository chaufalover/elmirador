
package com.proyecto.elmirador.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    
    private static final String URL = "jdbc:mysql://localhost:3306/elmirador";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USER = "root";
    private static final String PASS = "admin";
    private static ConexionBD instance;
    private Connection connection;

    private ConexionBD() {
        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error al inicializar la conexión a la base de datos: " + e.getMessage());
        }
    }
    
    public synchronized static ConexionBD getInstance() {
        if (instance == null) {
            instance = new ConexionBD();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la conexión: " + e.getMessage());
        }
        return connection;
    }
    
}
