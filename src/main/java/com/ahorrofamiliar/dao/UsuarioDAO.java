package com.ahorrofamiliar.dao;

import com.ahorrofamiliar.models.Usuario;
import com.ahorrofamiliar.utils.DatabaseConnection;
import static com.ahorrofamiliar.utils.DatabaseConnection.getConnection;
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
            String sql = "SELECT U.id AS ID, U.id_rol, U.nombre, U.apellido, T.NOMBRE AS Tipo_documento, U.Numero_doc, U.sexo, U.Situacion, U.Contrasenia FROM usuario U INNER JOIN tipo_documento T ON U.ID_Tipo_documento = T.ID WHERE Numero_doc = ? AND Contrasenia = ?";
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
                if (resultado != null) {
                    resultado.close();
                }
                if (statement != null) {
                    statement.close();
                }
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

    /*public boolean existeUsuario(int idUsuario) throws SQLException {
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
    }*/
    public boolean existeUsuario(int idUsuario) {  //se agrega por jimmy 11/05/25 para CONTRASENA
        String sql = "SELECT COUNT(*) FROM usuario WHERE id = ?";
        try (Connection conexion = getConnection(); PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, idUsuario);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar usuario: " + e.getMessage());
        }
        return false;
    

    ///hasta aca agregado por jimmy
    }

    //jimmy
    public boolean actualizarContrasena(int idUsuario, String nuevaContrasena) {
        Connection conexion = null;
        PreparedStatement statement = null;

        try {
            conexion = DatabaseConnection.getConnection();
            System.out.println("Estado conexión al iniciar: " + DatabaseConnection.getConnectionStatus());

            String sql = "UPDATE usuario SET Contrasenia = ? WHERE id = ?";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, nuevaContrasena);
            statement.setInt(2, idUsuario);

            int filasAfectadas = statement.executeUpdate();

            // Verificación inmediata
            if (filasAfectadas > 0) {
                System.out.println("Verificando actualizacion...");
                String contraseniaActual = obtenerContrasena(idUsuario);
                System.out.println("Contrasenia en BD después de actualizar: " + contraseniaActual);
            }

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error SQL al actualizar: " + e.getMessage());
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar statement: " + e.getMessage());
            }
            // No cerramos la conexión aquí para mantener compatibilidad
        }
    }

    public String obtenerContrasena(int idUsuario) throws SQLException {
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            conexion = DatabaseConnection.getConnection();
            String sql = "SELECT Contrasenia FROM usuario WHERE id = ?";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, idUsuario);
            rs = statement.executeQuery();

            return rs.next() ? rs.getString("Contrasenia") : null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    // Mantén tus otros métodos existentes sin cambios
    //jimmy
    public List<Usuario> listarUsuarios() throws Exception {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido FROM usuario WHERE situacion = 'A'"; // Solo activos, ajusta si quieres

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

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

    //inicio de cambios por Elizabeth Aguilar
    //metodo adicionar
    public void adicion(Usuario p) {
        Connection cn = null;

        try {
            cn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO usuario VALUES (null, ?,?,?,?,?,?,?,?)";
            PreparedStatement st = cn.prepareStatement(sql);
            st.setInt(1, p.getIdRol());
            st.setString(2, p.getNombre());
            st.setString(3, p.getApellido());
            st.setInt(4, p.getId_tip_doc());
            st.setString(5, p.getNumeroDocumento());
            st.setString(6, p.getSexo());
            st.setString(7, p.getSituacion());
            st.setString(8, p.getContrasenia());
            st.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //metodo modificar
    public void modifica(Usuario p) {
        Connection cn = null;
        try {
            cn = DatabaseConnection.getConnection();
            String sql = "UPDATE usuario set id_rol = ?, nombre = ?, apellido = ?, id_Tipo_documento = ?, Numero_doc = ?, sexo = ?, situacion = ? WHERE id = ?";
            PreparedStatement st = cn.prepareStatement(sql);
            st.setInt(1, p.getIdRol());
            st.setString(2, p.getNombre());
            st.setString(3, p.getApellido());
            st.setInt(4, p.getId_tip_doc());
            st.setString(5, p.getNumeroDocumento());
            st.setString(6, p.getSexo());
            st.setString(7, p.getSituacion());
            st.setLong(8, p.getId());

            st.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //metodo anular
    public void anular(int nro, String situacion) {
        Connection cn = null;
        try {
            cn = DatabaseConnection.getConnection();
            String sql = "UPDATE usuario SET  Situacion = ? where id = ?";
            PreparedStatement st = cn.prepareStatement(sql);
            st.setString(1, situacion);
            st.setInt(2, nro);
            st.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Usuario> ListarUsuario(String nombre, String apellido) {
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultado = null;
        String sql = null;
        Usuario usuario = null;
        List<Usuario> lista = new ArrayList();
        try {
            conexion = DatabaseConnection.getConnection();
            if (nombre.equals("") && apellido.equals("")) {
                sql = "SELECT U.id AS ID, R.nombre AS Rol, U.nombre, U.apellido, T.Nombre AS Tipo_documento, U.Numero_doc, U.sexo, U.Situacion "
                        + "FROM usuario U "
                        + "INNER JOIN tipo_documento T ON U.ID_Tipo_documento = T.id "
                        + "INNER JOIN rol R ON R.id = U.id_rol "
                        + "WHERE U.Situacion = 'A'";
                statement = conexion.prepareStatement(sql);
                resultado = statement.executeQuery();
            } else if (nombre.equals("") && !apellido.equals("")) {
                sql = "SELECT U.id AS ID, R.nombre AS Rol, U.nombre, U.apellido, T.Nombre AS Tipo_documento, U.Numero_doc, U.sexo, U.Situacion "
                        + "FROM usuario U "
                        + "INNER JOIN tipo_documento T ON U.ID_Tipo_documento = T.id "
                        + "INNER JOIN rol R ON R.id = U.id_rol "
                        + "WHERE U.Situacion = 'A' AND U.apellido LIKE CONCAT('%', ?, '%')";
                statement = conexion.prepareStatement(sql);
                statement.setString(1, apellido);
                resultado = statement.executeQuery();
            } else if (!nombre.equals("") && apellido.equals("")) {
                sql = "SELECT U.id AS ID, R.nombre AS Rol, U.nombre, U.apellido, T.Nombre AS Tipo_documento, U.Numero_doc, U.sexo, U.Situacion "
                        + "FROM usuario U "
                        + "INNER JOIN tipo_documento T ON U.ID_Tipo_documento = T.id "
                        + "INNER JOIN rol R ON R.id = U.id_rol "
                        + "WHERE U.Situacion = 'A' AND U.nombre LIKE CONCAT('%', ?, '%')";
                statement = conexion.prepareStatement(sql);
                statement.setString(1, nombre);
                resultado = statement.executeQuery();
            } else {
                sql = "SELECT U.id AS ID, R.nombre AS Rol, U.nombre, U.apellido, T.Nombre AS Tipo_documento, U.Numero_doc, U.sexo, U.Situacion "
                        + "FROM usuario U "
                        + "INNER JOIN tipo_documento T ON U.ID_Tipo_documento = T.id "
                        + "INNER JOIN rol R ON R.id = U.id_rol "
                        + "WHERE U.Situacion = 'A' AND U.nombre = ? AND U.apellido = ?";
                statement = conexion.prepareStatement(sql);
                statement.setString(1, nombre);
                statement.setString(2, apellido);
                resultado = statement.executeQuery();
            }

            while (resultado.next()) {
                usuario = new Usuario();
                usuario.setId(resultado.getInt("id"));
                usuario.setRol(resultado.getString("Rol"));
                usuario.setNombre(resultado.getString("nombre"));
                usuario.setApellido(resultado.getString("apellido"));
                usuario.setTipoDocumento(resultado.getString("Tipo_documento"));
                usuario.setNumeroDocumento(resultado.getString("Numero_doc"));
                usuario.setSexo(resultado.getString("sexo"));
                usuario.setSituacion(resultado.getString("Situacion"));
                lista.add(usuario);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el usuario por nombre y apellido: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (conexion != null) {
                    DatabaseConnection.closeConnection();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos de la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return lista;
    }

    public Usuario ValporDNI(String tipodoc, String numdoc) {
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultado = null;
        String sql = null;
        Usuario usuario = null;
        try {
            conexion = DatabaseConnection.getConnection();
            sql = "SELECT U.id AS ID, r.nombre as Rol, U.nombre, U.apellido, T.NOMBRE AS Tipo_documento, U.Numero_doc, U.sexo, U.Situacion FROM usuario U INNER JOIN TIPO_DOCUMENTO T ON U.ID_Tipo_documento = T.ID where u.situacion= 'A' and T.NOMBRE =? and U.Numero_doc = ?";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tipodoc);
            statement.setString(2, numdoc);
            resultado = statement.executeQuery();

            if (resultado.next()) {
                usuario = new Usuario();
                usuario.setId(resultado.getInt("id"));
                usuario.setRol(resultado.getString("Rol"));
                usuario.setNombre(resultado.getString("nombre"));
                usuario.setApellido(resultado.getString("apellido"));
                usuario.setTipoDocumento(resultado.getString("Tipo_documento"));
                usuario.setNumeroDocumento(resultado.getString("Numero_doc"));
                usuario.setSexo(resultado.getString("sexo"));
                usuario.setSituacion(resultado.getString("Situacion"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el usuario por nombre y apellido: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (conexion != null) {
                    DatabaseConnection.closeConnection();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos de la base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return usuario;
    }
//Fin de cambios por Elizabeth Aguilar

}
