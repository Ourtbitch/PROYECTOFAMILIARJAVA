package com.ahorrofamiliar.database;

import com.ahorrofamiliar.models.Usuario;
import com.ahorrofamiliar.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario obtenerUsuarioPorNombreUsuario(String nombreUsuario) {
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Usuario usuario = null;

        try {
            conexion = DatabaseConnection.getConnection();
            String sql = "SELECT id, id_rol, nombre, apellido, Tipo_documento, Numero_doc, sexo, Situacion, Contrasenia FROM usuario WHERE nombre = ?";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, nombreUsuario);
            resultado = statement.executeQuery();

            if (resultado.next()) {
                usuario = new Usuario();
                usuario.setId(resultado.getInt("id"));
                usuario.setIdRol(resultado.getInt("id_rol"));
                usuario.setNombre(resultado.getString("nombre"));
                usuario.setApellido(resultado.getString("apellido"));
                usuario.setTipoDocumento(resultado.getString("Tipo_documento"));
                usuario.setNumeroDocumento(resultado.getString("Numero_doc"));
                usuario.setSexo(resultado.getString("sexo"));
                usuario.setSituacion(resultado.getString("Situacion"));
                usuario.setContrasenia(resultado.getString("Contrasenia"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el usuario por nombre de usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultado != null) resultado.close();
                if (statement != null) statement.close();
                // La conexión se cierra en AuthService o en un filtro/interceptor en aplicaciones más complejas
                // Para esta etapa inicial, podemos cerrarla aquí después de cada operación.
                if (conexion != null) DatabaseConnection.closeConnection();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos de la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return usuario;
    }
}