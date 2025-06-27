/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ahorrofamiliar.dao;

import com.ahorrofamiliar.models.MetaTraslado;
import com.ahorrofamiliar.utils.DatabaseConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class MetaTrasladoDAO {

    // Realizar traslado de saldo
    public void realizarTraslado(MetaTraslado traslado) throws SQLException {
        String procedimiento = "{ CALL Trasladar_Saldo_Meta(?, ?, ?, ?) }";

        try (Connection con = DatabaseConnection.getConnection(); CallableStatement cs = con.prepareCall(procedimiento)) {

            cs.setInt(1, traslado.getIdMetaOrigen());
            cs.setInt(2, traslado.getIdMetaDestino());
            cs.setDouble(3, traslado.getMonto());
            cs.setInt(4, traslado.getIdUsuario());
            cs.execute();
        }
    }

    // Listar historial de traslados
    public List<MetaTraslado> listarHistorialTraslados() throws SQLException {
        List<MetaTraslado> lista = new ArrayList<>();
        String sql = "SELECT t.id, t.id_meta_origen, t.id_meta_destino, t.monto, "
                + "t.fecha_traslado, t.id_usuario, "
                + "mo.nombre_meta as nombre_meta_origen, "
                + "md.nombre_meta as nombre_meta_destino, "
                + "CONCAT(u.nombre, ' ', u.apellido) as nombre_usuario "
                + "FROM meta_traslados t "
                + "JOIN meta mo ON t.id_meta_origen = mo.id "
                + "JOIN meta md ON t.id_meta_destino = md.id "
                + "JOIN usuario u ON t.id_usuario = u.id "
                + "ORDER BY t.fecha_traslado DESC";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MetaTraslado traslado = new MetaTraslado();
                traslado.setId(rs.getInt("id"));
                traslado.setIdMetaOrigen(rs.getInt("id_meta_origen"));
                traslado.setIdMetaDestino(rs.getInt("id_meta_destino"));
                traslado.setMonto(rs.getDouble("monto"));
                traslado.setFechaTraslado(rs.getTimestamp("fecha_traslado"));
                traslado.setIdUsuario(rs.getInt("id_usuario"));
                traslado.setNombreMetaOrigen(rs.getString("nombre_meta_origen"));
                traslado.setNombreMetaDestino(rs.getString("nombre_meta_destino"));
                traslado.setNombreUsuario(rs.getString("nombre_usuario"));
                lista.add(traslado);
            }
        }
        return lista;
    }

    // Obtener saldo disponible de una meta
    public double obtenerSaldoDisponible(int idMeta) throws SQLException {
        String sql = "SELECT Importe_final_r FROM meta WHERE id = ?";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idMeta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("Importe_final_r");
                }
            }
        }
        return 0;
    }
}
