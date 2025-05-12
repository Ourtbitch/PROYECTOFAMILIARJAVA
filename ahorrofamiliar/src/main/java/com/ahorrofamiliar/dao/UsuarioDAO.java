package com.ahorrofamiliar.dao;

import com.ahorrofamiliar.models.Usuario;
import com.ahorrofamiliar.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    public Usuario obtenerUsuarioPorDocumentoYContrasenia(String numeroDoc, String contrasenia) {
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultado = null;
        Usuario usuario = null;

        try {
            conexion = DatabaseConnection.getConnection();
            //Cambio Misael 11-05 ini - Se modifica consulta para que soo filtre por dni y contrasena
            String sql = "SELECT U.id AS ID, U.id_rol, U.nombre, U.apellido, T.NOMBRE AS Tipo_documento, U.Numero_doc, U.sexo, U.Situacion, U.Contrasenia FROM usuario U INNER JOIN tipo_documento T ON U.ID_Tipo_documento = T.ID WHERE Numero_doc = ? AND Contrasenia = ?" ;
            //Cambio Misael 11-05 fin
            statement = conexion.prepareStatement(sql);
            statement.setString(1, numeroDoc);
            statement.setString(2, contrasenia);
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
            System.err.println("Error al obtener el usuario por documento y contraseña: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultado != null) resultado.close();
                if (statement != null) statement.close();
                /*Misael Challco 11-05 -- INI - se quita el metodo closeConnection 
                para que no se cierre cada que se ejecuta
                if (conexion != null) DatabaseConnection.closeConnection();
               Misael Challco 11-05 -- FIN - se quita el metodo closeConnection   
                */
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos de la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return usuario;
    }
    public boolean existeUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
     public List<Usuario> listarUsuarios() throws Exception {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido FROM usuario WHERE situacion = 'ACT'"; // Solo activos, ajusta si quieres

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                lista.add(usuario);
            }
        } catch (Exception e) {
            throw new Exception("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }
}

