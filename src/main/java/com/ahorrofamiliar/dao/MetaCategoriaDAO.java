/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ahorrofamiliar.dao;

import com.ahorrofamiliar.models.MetaCategoria;
import com.ahorrofamiliar.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class MetaCategoriaDAO {

    // Listar todas las categorías activas
    public List<MetaCategoria> listarCategoriasActivas() throws SQLException {
        List<MetaCategoria> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM meta_categoria WHERE situacion = 'A'";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MetaCategoria categoria = new MetaCategoria();
                categoria.setId(rs.getInt("id"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setDescripcion(rs.getString("descripcion"));
                lista.add(categoria);
            }
        }
        return lista;
    }

    // Obtener categoría por ID
    public MetaCategoria obtenerPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, descripcion, situacion FROM meta_categoria WHERE id = ?";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MetaCategoria categoria = new MetaCategoria();
                    categoria.setId(rs.getInt("id"));
                    categoria.setNombre(rs.getString("nombre"));
                    categoria.setDescripcion(rs.getString("descripcion"));
                    categoria.setSituacion(rs.getString("situacion"));
                    return categoria;
                }
            }
        }
        return null;
    }

    // Crear nueva categoría
    public void crearCategoria(MetaCategoria categoria) throws SQLException {
        String sql = "INSERT INTO meta_categoria (nombre, descripcion, situacion) VALUES (?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setString(3, categoria.getSituacion());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setId(rs.getInt(1));
                }
            }
        }
    }

    // Actualizar categoría
    public void actualizarCategoria(MetaCategoria categoria) throws SQLException {
        String sql = "UPDATE meta_categoria SET nombre = ?, descripcion = ? WHERE id = ?";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setInt(3, categoria.getId());
            ps.executeUpdate();
        }
    }

    // Cambiar estado de categoría (activar/desactivar)
    public void cambiarEstadoCategoria(int id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE meta_categoria SET situacion = ? WHERE id = ?";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void eliminarCategoria(int id) throws SQLException {
        String sql = "DELETE FROM meta_categoria WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<MetaCategoria> listarTodas() throws SQLException {
        List<MetaCategoria> lista = new ArrayList<>();

        String sql = "SELECT id, nombre, descripcion, situacion FROM meta_categoria";

        try (Connection conn = DatabaseConnection.getConnection(); 
                 PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MetaCategoria cat = new MetaCategoria();
                cat.setId(rs.getInt("id"));
                cat.setNombre(rs.getString("nombre"));
                cat.setDescripcion(rs.getString("descripcion"));
                cat.setSituacion(rs.getString("situacion"));
                lista.add(cat);
            }
        }

        return lista;
    }
}
