/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ahorrofamiliar.dao;

import com.ahorrofamiliar.models.GestionContrasena;
import com.ahorrofamiliar.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestionContrasenaDAO {

    public List<GestionContrasena> obtenerHistorialContrasena(int idUsuario) {
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<GestionContrasena> historial = new ArrayList<>();

        try {
            conexion = DatabaseConnection.getConnection();
            String sql = "SELECT id_gestion, contrasena_hash, fecha_cambio, tipo_cambio, realizado_por "
                    + "FROM gestion_contrasena WHERE id_usuario = ? ORDER BY fecha_cambio DESC";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, idUsuario);
            resultado = statement.executeQuery();

            while (resultado.next()) {
                GestionContrasena gestion = new GestionContrasena();
                gestion.setIdGestion(resultado.getInt("id_gestion"));
                gestion.setIdUsuario(idUsuario);
                gestion.setContrasenaHash(resultado.getString("contrasena_hash"));
                gestion.setFechaCambio(resultado.getDate("fecha_cambio"));
                gestion.setTipoCambio(resultado.getString("tipo_cambio"));
                gestion.setRealizadoPor(resultado.getString("realizado_por"));

                historial.add(gestion);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener historial de contraseña: " + e.getMessage());
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
                System.err.println("Error al cerrar recursos: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return historial;
    }

    public boolean registrarCambioContrasena(GestionContrasena gestion) {
        Connection conexion = null;
        PreparedStatement statement = null;
        boolean exito = false;

        try {
            conexion = DatabaseConnection.getConnection();
            String sql = "INSERT INTO gestion_contrasena (id_usuario, contrasena_hash, fecha_cambio, tipo_cambio, realizado_por) "
                    + "VALUES (?, ?, ?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, gestion.getIdUsuario());
            statement.setString(2, gestion.getContrasenaHash());
            statement.setDate(3, new java.sql.Date(gestion.getFechaCambio().getTime()));
            statement.setString(4, gestion.getTipoCambio());
            statement.setString(5, gestion.getRealizadoPor());

            int filasAfectadas = statement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar cambio de contraseña: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conexion != null) {
                    DatabaseConnection.closeConnection();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return exito;
    }

}
