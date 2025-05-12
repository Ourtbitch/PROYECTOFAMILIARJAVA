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
}
