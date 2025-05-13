/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ahorrofamiliar.dao;

/**
 *
 * @author pisarana
 */
import com.ahorrofamiliar.models.Meta;
import com.ahorrofamiliar.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetaDAO {

    /*
     * Verifica si existe una meta con el id dado.
     * @param idMeta El ID de la meta a verificar.
     * @return true si existe, false si no.
     * @throws SQLException
     */
    public boolean existeMeta(int idMeta) throws SQLException {
        String sql = "SELECT COUNT(*) FROM meta WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMeta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /*
     * Actualiza el monto acumulado real (Importe_final_r) en la tabla meta,
     * sumando todos los aportes reales de meta_aporte para esa meta.
     * @param idMeta El ID de la meta a actualizar.
     * @throws SQLException
     */
    public void actualizarMontoAcumulado(int idMeta) throws SQLException {
        String sql = "UPDATE meta SET Importe_final_r = ("
                + "SELECT IFNULL(SUM(aporte_real), 0) FROM meta_aporte WHERE id_meta = ?"
                + ") WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMeta);
            ps.setInt(2, idMeta);
            ps.executeUpdate();
        }
    }

    public List<Meta> listarMetas() throws SQLException {
        List<Meta> lista = new ArrayList<>();
        String sql = "SELECT id, nombre_meta, Importe_final_r FROM meta"; // Ajusta columnas según tu tabla

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Meta meta = new Meta();
                meta.setId(rs.getInt("id"));
                meta.setNombre_meta(rs.getString("nombre_meta")); // coincide con setNombre_meta
                meta.setImporte_Final_r(rs.getDouble("Importe_final_r")); // coincide con setImporte_Final_r
                lista.add(meta);
            }
        }
        return lista;
    }
    
    
    public List<Meta> consulta(String nombre,String estado) {

        List<Meta> lista = new ArrayList();
        Meta p = null;
        String sql = null;
        Connection cn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            cn = DatabaseConnection.getConnection();
            if (nombre.equals("") &&  estado.equals("") ) {
                sql = "SELECT id,nombre_meta,Importe,fecha_creacion,Fecha_fin,Estado FROM meta where situacion= 'A'";
                statement = cn.prepareStatement(sql);
                rs = statement.executeQuery();
            } else if (nombre.equals("") &&  estado!="" ) {
                sql = "SELECT id,nombre_meta,Importe,fecha_creacion,Fecha_fin,Estado FROM meta where situacion= 'A' and Estado= ?";
                statement = cn.prepareStatement(sql);
                statement.setString(1, estado);
                rs = statement.executeQuery();
            }else if (estado.equals("") &&  nombre !="" ) {
                sql = "SELECT id,nombre_meta,Importe,fecha_creacion,Fecha_fin,Estado FROM meta where situacion= 'A' and nombre like CONCAT('%', ?, '%')";
                statement = cn.prepareStatement(sql);
                statement.setString(1, nombre);
                rs = statement.executeQuery();
            }else {
                sql = "SELECT id,nombre_meta,Importe,fecha_creacion,Fecha_fin,Estado FROM meta where situacion= 'A'and nombre_meta like CONCAT('%', ?, '%') and Estado= ?";
                statement = cn.prepareStatement(sql);
                statement.setString(1, nombre);
                statement.setString(2, estado);
                rs = statement.executeQuery();
            }

            while (rs.next()) {
                p = new Meta();
                p.setId(rs.getInt(1));
                p.setNombre_meta(rs.getString(2));
                p.setImporte(rs.getDouble(3));
                p.setFecha_creacion(rs.getDate(4));
                p.setFecha_fin(rs.getDate(5));
                p.setEstado(rs.getString(6));
                lista.add(p);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    //metodo adicionar
    public void adicion(Meta p) {
        Connection cn = null;

        try {
            cn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO meta VALUES (null,?,?,?,?,?,?,?,?,?,?) ";
            PreparedStatement st = cn.prepareStatement(sql);
            st.setString(1, p.getNombre_meta());
            st.setDouble(2, p.getImporte());

            java.util.Date utilDate = p.getFecha_creacion();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            st.setDate(3, sqlDate);

            utilDate = p.getFecha_fin();
            sqlDate = new java.sql.Date(utilDate.getTime());
            st.setDate(4, sqlDate);

            st.setString(5, p.getEstado());
            st.setDouble(6, p.getImporte_Inicial_e());
            st.setDouble(7, p.getImporte_Final_e());
            st.setDouble(8, p.getImporte_Inicial_r());
            st.setDouble(9, p.getImporte_Final_r());
            st.setString(10, p.getSituacion());

            st.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //metodo modificar
    public void modifica(Meta p) {
        Connection cn = null;
        try {
            cn = DatabaseConnection.getConnection();
            String sql = "UPDATE meta set nombre_meta = ?, Importe = ?, Estado = ?,Fecha_Fin = ? WHERE id = ?";
            PreparedStatement st = cn.prepareStatement(sql);
            st.setString(1, p.getNombre_meta());
            st.setDouble(2, p.getImporte());
            st.setString(3, p.getEstado());
            java.util.Date utilDate = p.getFecha_fin();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            st.setDate(4, sqlDate);
            st.setLong(5, p.getId());

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
            String sql = "UPDATE meta SET  Situacion = ? where id = ?";
            PreparedStatement st = cn.prepareStatement(sql);
            st.setString(1, situacion);
            st.setInt(2, nro);
            st.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
