package com.ahorrofamiliar.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost/bd_ahorro?serverTimezone=America/Lima";
    private static final String USUARIO = "root"; 
    private static final String CONTRASENIA = "yourpassword"; 
    private static Connection conexion;
    private static boolean driverCargado = false;

    // Carga el driver solo una vez
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            driverCargado = true;
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver JDBC: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        synchronized (DatabaseConnection.class) {
            if (!driverCargado) {
                throw new SQLException("Driver JDBC no cargado correctamente");
            }
            
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENIA);
                System.out.println("Conexión establecida: " + conexion);
            }
            return conexion;
        }
    }

    public static void closeConnection() {
        synchronized (DatabaseConnection.class) {
            if (conexion != null) {
                try {
                    if (!conexion.isClosed()) {
                        conexion.close();
                        System.out.println("Conexión cerrada correctamente");
                    }
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                } finally {
                    conexion = null;
                }
            }
        }
    }

    // Método para verificar el estado (solo para diagnóstico)
    public static String getConnectionStatus() {
        synchronized (DatabaseConnection.class) {
            if (conexion == null) return "No hay conexión";
            try {
                return conexion.isClosed() ? "Conexión cerrada" : "Conexión activa";
            } catch (SQLException e) {
                return "Error al verificar estado: " + e.getMessage();
            }
        }
    }
}