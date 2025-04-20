package com.ahorrofamiliar.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/bd_ahorro?serverTimezone=America/Lima";
    private static final String USUARIO = "root"; // Reemplaza con tu usuario de MySQL
    private static final String CONTRASENIA = "yourpassword"; // Reemplaza con tu contraseña de MySQL
    private static Connection conexion;

    private DatabaseConnection() {
        // Constructor privado para evitar instanciación directa
    }

    public static Connection getConnection() {
        if (conexion == null) {
            synchronized (DatabaseConnection.class) {
                if (conexion == null) {
                    try {
                        // Cargar el driver de MySQL (opcional en versiones recientes)
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENIA);
                        System.out.println("Conexión a la base de datos exitosa.");
                    } catch (ClassNotFoundException e) {
                        System.err.println("Error: No se encontró el driver de MySQL.");
                        e.printStackTrace();
                    } catch (SQLException e) {
                        System.err.println("Error al conectar a la base de datos.");
                        e.printStackTrace();
                    }
                }
            }
        }
        return conexion;
    }

    public static void closeConnection() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión a la base de datos cerrada.");
                conexion = null;
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión a la base de datos.");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // Método de prueba para verificar la conexión
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            DatabaseConnection.closeConnection();
        }
    }
}