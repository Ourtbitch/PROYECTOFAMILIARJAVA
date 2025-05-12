package com.ahorrofamiliar.service;

import com.ahorrofamiliar.dao.UsuarioDAO;
import com.ahorrofamiliar.models.Usuario;
import com.ahorrofamiliar.utils.DatabaseConnection; // Import para cerrar la conexión si es necesario

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    
    //Misael Challco 11/05 - INI - Se agrega logica para implementar Maximo intentos fallidos
    private static final int MAX_INTENTOS = 3;
    private Map<String, Integer> intentosFallidos = new HashMap<>();
     //Misael Challco 11/05 - Fin - Se agrega logica para implementar Maximo intentos fallidos
    private UsuarioDAO usuarioDAO;

    public AuthService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario autenticarUsuarioPorDni(String dni, String contrasenia) {
        
        //Misael Challco 11/05 - INI - Se agrega logica para implementar Maximo intentos fallidos
        Integer intentos = intentosFallidos.getOrDefault(dni, 0);
        if (intentos >= MAX_INTENTOS){
            System.out.print("Usuario bloqueado temporalmente por demasiados intentos fallidos.");
            return null; // Bloqueo temporal
        }
        //Misael Challco 11/05 - Fin - Se agrega logica para implementar Maximo intentos fallidos
        
        Usuario usuario = usuarioDAO.obtenerUsuarioPorDocumentoYContrasenia(dni, contrasenia);
        
        
        if (usuario != null && usuario.getContrasenia().equals(contrasenia)) {
            //Misael Challco 11/05 - INI - Se resetea Intentos Fallidos
            intentosFallidos.remove(dni); 
            //Misael Challco 11/05 - Fin - Se resetea Intentos Fallidos
            return usuario;
        } else {
            intentosFallidos.put(dni, intentos + 1);
             //Misael Challco 11/05 - INI - Se  agregra print a consola
            System.out.println("Intento fallido #" + (intentos + 1) + " para usuario: " + dni);
            //Misael Challco 11/05 - FIN - Se  agregra print a consola
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