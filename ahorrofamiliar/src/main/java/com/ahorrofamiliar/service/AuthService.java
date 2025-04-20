package com.ahorrofamiliar.service;

import com.ahorrofamiliar.database.UsuarioDAO;
import com.ahorrofamiliar.models.Usuario;
import com.ahorrofamiliar.utils.DatabaseConnection; // Import para cerrar la conexión si es necesario

import java.sql.Connection;
import java.sql.SQLException;

public class AuthService {

    private UsuarioDAO usuarioDAO;

    public AuthService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario autenticarUsuario(String nombre, String apellido, String contrasenia) {
        Usuario usuario = usuarioDAO.obtenerUsuarioPorNombreApellido(nombre, apellido);

        if (usuario != null && usuario.getContrasenia().equals(contrasenia)) {
            return usuario;
        } else {
            return null; // Autenticación fallida
        }
    }

    // Método para cerrar la conexión a la base de datos cuando sea necesario
    public void cerrarConexion() {
        try {
            Connection conexion = DatabaseConnection.getConnection();
            if (conexion != null && !conexion.isClosed()) {
                DatabaseConnection.closeConnection();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar el estado de la conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}